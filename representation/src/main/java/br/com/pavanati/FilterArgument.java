package br.com.pavanati;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterArgument {
    private BooleanBuilder booleanBuilder;
}

