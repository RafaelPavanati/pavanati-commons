package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;

import java.math.BigDecimal;
import java.util.Arrays;

import static br.com.pavanati.RepresentationResolver.*;

public class NumberPathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        NumberPath<BigDecimal> expression = (NumberPath) exp;
        if (operador.equals(IN)) {
            return expression.in(Arrays.stream(value.split(COMMA_PATTERN))
                    .map(number -> BigDecimal.valueOf(Double.parseDouble(number)).setScale(2))
                    .toList());
        }
        if (operador.equals(NOT_IN)) {
            return expression.notIn(Arrays.stream(value.split(COMMA_PATTERN))
                    .map(number -> BigDecimal.valueOf(Double.parseDouble(number)).setScale(2))
                    .toList());
        }
        BigDecimal data = BigDecimal.valueOf(Double.parseDouble(value)).setScale(2);

        return switch (operador) {
            case EQ -> expression.eq(data);
            case LT -> expression.lt(data);
            case GOE -> expression.goe(data);
            case LOE -> expression.loe(data);
            default -> null;
        };
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof NumberPath;
    }
}
