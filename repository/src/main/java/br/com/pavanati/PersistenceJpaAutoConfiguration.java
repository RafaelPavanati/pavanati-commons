package br.com.pavanati;

import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAProvider;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PersistenceJpaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JPQLTemplates jpqlTemplates(EntityManager em) {
        return JPAProvider.getTemplates(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public PathBuilderFactory pathBuilderFactory() {
        return new PathBuilderFactory();
    }
}
