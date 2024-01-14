package br.com.pavanati;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationParams implements Pageable {
    Integer page;
    Integer size;
    List<String> orderBy;
    List<String> sortBy;

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return (long) page * size;
    }

    @Override
    public Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>();
        if (orderBy != null) {
            for (int index = 0; index < orderBy.size(); index++) {
                orders.add(new Sort.Order(
                        sortBy != null && sortBy.get(index) != null
                                ? Sort.Direction.valueOf(sortBy.get(index))
                                : Sort.Direction.DESC,
                        orderBy.get(index)));
            }
        }
        return Sort.by(orders);
    }

    @Override
    public Pageable next() {
        return PaginationParams.builder().page(page).size(size).orderBy(orderBy).sortBy(sortBy).build();
    }

    @Override
    public Pageable previousOrFirst() {
        return PaginationParams.builder().page(page == 0 ? 0 : page - 1).size(size).orderBy(orderBy).sortBy(sortBy)
                .build();
    }

    @Override
    public Pageable first() {
        return PaginationParams.builder().page(0).size(size).orderBy(orderBy).sortBy(sortBy).build();
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return page == null || page == 0;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }

}
