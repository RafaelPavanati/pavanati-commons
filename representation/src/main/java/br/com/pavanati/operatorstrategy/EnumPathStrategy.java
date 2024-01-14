package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;

import java.util.Arrays;

import static br.com.pavanati.RepresentationResolver.*;

public class EnumPathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        EnumPath expression = (EnumPath) exp;

        if (operador.equals(EQ)) {
            return expression.eq(Arrays.stream(expression.getType().getEnumConstants())
                .filter(anEnum -> anEnum.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null));
        }
        if (operador.equals(NE)) {
            return expression.ne(Arrays.stream(expression.getType().getEnumConstants())
                .filter(anEnum -> anEnum.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null));
        }
        if (operador.equals(IN)) {
            return expression.in(Arrays.stream(value.split(","))
                .filter(s -> !s.isEmpty())
                .map(s ->  Arrays.stream(expression.getType().getEnumConstants())
                .filter(anEnum -> anEnum.toString().equalsIgnoreCase(s))
                .findFirst()
                .orElse(null)).toList());
        }
        if (operador.equals(NOT_IN)) {
            return expression.notIn(Arrays.stream(value.split(","))
                    .filter(s -> !s.isEmpty())
                    .map(s ->  Arrays.stream(expression.getType().getEnumConstants())
                            .filter(anEnum -> anEnum.toString().equalsIgnoreCase(s))
                            .findFirst()
                            .orElse(null)).toList());
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof EnumPath;
    }
}
