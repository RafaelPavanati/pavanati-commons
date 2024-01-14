package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.DatePath;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static br.com.pavanati.RepresentationResolver.*;

public class LocalDatePathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        DateExpression<LocalDate> expression = (DateExpression) exp;

        if (operador.equals(IN) || operador.equals(NOT_IN)) {
            List<LocalDate> localDates = Arrays.stream(value.split(COMMA_PATTERN)).map(this::parse).toList();
            return getOperador(expression, operador, localDates);
        }

        return getOperador(expression, operador, parse(value));
    }

    private LocalDate parse(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d/M/yyyy][yyyy-MM-dd]");

        return LocalDate.parse(value.replace(" ", ""), formatter);
    }

    private BooleanExpression getOperador(DateExpression<LocalDate> expression, String operador, LocalDate localDate) {
        if (operador.equals(EQ)) {
            return expression.eq(localDate);
        }
        if (operador.equals(GT)) {
            return expression.gt(localDate);
        }
        if (operador.equals(LT)) {
            return expression.lt(localDate);
        }
        if (operador.equals(GOE)) {
            return expression.goe(localDate);
        }
        if (operador.equals(LOE)) {
            return expression.loe(localDate);
        }

        return null;
    }

    private BooleanExpression getOperador(DateExpression<LocalDate> expression, String operador, List<LocalDate> localDates) {
        if (operador.equals(IN)) {
            return expression.in(localDates);
        }
        if (operador.equals(NOT_IN)) {
            return expression.notIn(localDates);
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof DatePath;
    }
}
