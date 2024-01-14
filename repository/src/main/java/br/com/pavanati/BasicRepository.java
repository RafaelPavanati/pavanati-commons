package br.com.pavanati;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class BasicRepository {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory jpaQueryFactory;

    @Autowired
    PathBuilderFactory pathBuilderFactory;

    @PostConstruct
    public void init() {
        jpaQueryFactory = new JPAQueryFactory(this.em);
    }


    public EntityManager getEm() {
        return em;
    }

    public <T> T find(Class<T> entityClass, Serializable id) {
        return em.find(entityClass, id);
    }

    public <T> CloseableIterator<T> iterator(final Class<T> entityClass, final Predicate... where) {
        return iterator(entityClass, false, where);
    }

    public <T> CloseableIterator<T> iterator(final Class<T> entityClass, boolean skipContext, final Predicate... where) {
        JPAQuery<?> query = query(entityClass, skipContext).where(where);
        return (CloseableIterator<T>) query.iterate();

    }

    public <T> PagResultJson<T> findAll(Class<T> entityClass, PaginationParams pageable, Predicate... where) {
        return this.findAll(entityClass, pageable, null, where);
    }

    public <T> PagResultJson<T> findAll(final Class<T> entityClass, final PaginationParams pageable, QBean<?> select, final Predicate... where) {
        JPAQuery<?> query = query(entityClass).where(where);
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long total = query.fetchCount();
        Long totalPages = total > 0 ? limit / total : 0;

        query.offset(offset);
        if (Objects.nonNull(select)) {
            query.select(select);
        }
        query.limit(limit);

        applySorting(query, pageable.getSort(), entityClass);

        List<T> content = (List<T>) query.fetch();


        return new PagResultJson(content, limit, totalPages.intValue(), pageable.getPage(), total);
    }


    public <T> List<T> findAll(Class<T> entityClass, Predicate... where) {
        return this.findAll(entityClass, (Sort) null, where);
    }

    public <T> List<T> findAll(Class<T> entityClass, Sort sort, Predicate... where) {
        return this.applySorting((JPAQuery) this.query(entityClass).where(where), sort).fetch();
    }

    public <T> Page<T> findAll(Class<T> entityClass, Pageable pageable, Predicate... where) {
        return this.findAll(entityClass, pageable, false, where);
    }

    public <T> Page<T> findAll(Class<T> entityClass, Pageable pageable, boolean skipContextFilter, Predicate... where) {
        JPAQuery<T> query = (JPAQuery) this.query(entityClass, skipContextFilter).where(where);
        long total = query.fetchCount();
        int offset = pageable.getOffset();
        int limit = pageable.getLimit();
        if ((long) offset > total) {
            offset = (int) (total / (long) limit) * limit;
        } else if ((long) offset == total) {
            offset = Math.max(offset - limit, 0);
        }

        query.offset((long) offset);
        query.limit((long) limit);
        this.applySorting(query, pageable.getSort());
        List<T> content = query.fetch();
        return new Page(content, pageable, total);
    }

    public <T> Page<T> findAll(Class<T> entityClass, Consumer<JPAQuery> consumer, Pageable pageable, Predicate... where) {
        PathBuilder<T> entityPath = this.pathBuilderFactory.create(entityClass);
        JPAQuery query = this.query(entityClass);
        consumer.accept(query);
        query.where(where);
        long total = query.fetchCount();
        if (total == 0L) {
            return Page.empty();
        } else {
            this.applyPagination(query, pageable, total);
            List<T> content = query.select(entityPath).fetch();
            return new Page(content, pageable, total);
        }
    }

    public <T> T findOne(Class<T> entityClass, Predicate... where) {
        return this.findOne(entityClass, false, where);
    }

    public <T> T findOne(Class<T> entityClass, boolean skipContext, Predicate... where) {
        return this.findOne(entityClass, (Sort) null, skipContext, where);
    }

    public long count(Class entityClass, Predicate... where) {
        JPAQuery query = this.query(entityClass);
        query.where(where);
        return query.fetchCount();
    }

    public <T> T findOne(Class<T> entityClass, Sort sort, boolean skipContext, Predicate... where) {
        return (T) ((JPAQuery) this.applySorting((JPAQuery) this.query(entityClass, skipContext).where(where), sort).limit(1L)).fetchOne();
    }

    public <T> JPAQuery<?> applySorting(JPAQuery<?> query, org.springframework.data.domain.Sort sort, Class<T> entityClass) {
        PathBuilder<T> pathBuilder = this.pathBuilderFactory.create(entityClass);
        sort.forEach(order ->
                query.orderBy(new OrderSpecifier(order.getDirection().isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty()))));

        return query;
    }


    public <T> JPAQuery<T> applySorting(JPAQuery<T> query, Sort sort) {
        Optional.ofNullable(sort)
                .ifPresent(s -> s.forEach(query::orderBy));
        return query;
    }


    public <T extends Entity<?>> List<T> saveAll(Collection<T> entities) {
        return entities.stream().map(this::save).collect(Collectors.toList());
    }

    public <T extends Entity<?>> T save(T entity) {
        if (Objects.nonNull(entity.getId())) {
            this.em.merge(entity);
        } else {
            this.em.persist(entity);
        }
        this.em.flush();
        return entity;
    }

    public <T extends Entity<?>> T saveNew(T entity) {
        this.em.persist(entity);
        this.em.flush();
        return entity;
    }

    public <T> JPAQuery<T> query(Class<T> entityClass) {
        return this.query(entityClass, false);
    }

    public <T> JPAQuery<T> query(Class<T> entityClass, boolean skipContextFilter) {
        JPAQuery<T> query = new JPAQuery(this.em);
        query.from(this.pathBuilderFactory.create(entityClass));
        return query;
    }

    public <T> long update(Class<T> entityClass, Consumer<JPAUpdateClause> consumer) {
        PathBuilder<T> entityPath = pathBuilderFactory.create(entityClass);
        JPAUpdateClause clause = new JPAUpdateClause(em, entityPath);
        consumer.accept(clause);
        return clause.execute();
    }

    public <T> JPAQuery<T> applyPagination(JPAQuery<T> query, Pageable pageable, long total) {
        int offset = pageable.getOffset();
        int limit = pageable.getLimit();
        if ((long) offset > total) {
            offset = (int) (total / (long) limit) * limit;
        } else if ((long) offset == total) {
            offset = Math.max(offset - limit, 0);
        }

        query.offset((long) offset);
        query.limit((long) limit);
        this.applySorting(query, pageable.getSort());
        return query;
    }

    public <T> long delete(Class<T> entityClass, Consumer<JPADeleteClause> consumer) {
        PathBuilder<T> entityPath = this.pathBuilderFactory.create(entityClass);
        JPADeleteClause clause = (new JPADeleteClause(this.em, entityPath));
        consumer.accept(clause);
        return clause.execute();
    }

    public <T> long delete(Class<T> entityClass, Predicate... predicates) {
        return this.delete(entityClass, (query) -> query.where(predicates));
    }

    public <T> boolean exists(Class<T> entityClass, Predicate... predicates) {
        return this.query(entityClass, false).where(predicates).fetchCount() > 0;
    }
}
