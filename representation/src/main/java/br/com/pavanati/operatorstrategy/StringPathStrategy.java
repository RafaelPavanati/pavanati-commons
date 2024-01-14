package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;

import java.util.Arrays;

import static br.com.pavanati.RepresentationResolver.*;

public class StringPathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        StringExpression expression = (StringExpression) exp;
        if (operador.equals(EQ)) {
            value = value.replaceAll(CLEAN_PATTERN, EMPTY);
            if (value.contains(EXCLA)) {
                return expression.containsIgnoreCase(value.replace(EXCLA, EMPTY));
            }
            return expression.eq(value);
        }
        if (operador.equals(NE)) {
            value = value.replaceAll(CLEAN_PATTERN, EMPTY);
            return expression.notEqualsIgnoreCase(value.replace(EXCLA, EMPTY));
        }
        if (operador.equals(IN)) {
            value = value.replaceAll(CLEAN_PATTERN, EMPTY);
            return expression.in(Arrays.stream(value.split(COMMA_PATTERN)).toList());
        }
        if (operador.equals(NOT_IN)) {
            value = value.replaceAll(CLEAN_PATTERN, EMPTY);
            return expression.notIn(Arrays.stream(value.split(COMMA_PATTERN)).toList());
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof StringPath;
    }
}
