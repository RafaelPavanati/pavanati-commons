package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.SimplePath;

import java.util.Arrays;
import java.util.UUID;

import static br.com.pavanati.RepresentationResolver.*;

public class UuidPathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        if (operador.equals(IN)) {
            SimplePath<UUID> expression = (SimplePath) exp;
            return expression.in(Arrays.stream(value.split(COMMA_PATTERN))
                    .map(UUID::fromString).toList());
        }
        if (operador.equals(NOT_IN)) {
            SimplePath<UUID> expression = (SimplePath) exp;
            return expression.notIn(Arrays.stream(value.split(COMMA_PATTERN))
                    .map(UUID::fromString).toList());
        }
        if (operador.equals(EQ)) {
            ComparablePath<UUID> expression = (ComparablePath) exp;
            return expression.eq(UUID.fromString(value));
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression.getType().equals(UUID.class);
    }
}
