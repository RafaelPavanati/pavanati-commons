package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static br.com.pavanati.RepresentationResolver.*;

public class LocalDateTimePathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        DateTimeExpression<LocalDateTime> expression = (DateTimeExpression) exp;
        if (operador.equals(IN) || operador.equals(NOT_IN)) {
            List<LocalDateTime> datetimes = Arrays.stream(value.split(COMMA_PATTERN)).map(this::parse).toList();
            return getOperador(expression, operador, datetimes);
        }

        return getOperador(expression, operador, parse(value));
    }

    private LocalDateTime parse(String value) {
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(value, formatter).atStartOfDay();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm][yyyy-MM-dd HH][yyyy-MM-dd]");
            return LocalDateTime.parse(value.replaceAll("[ T]", " "), formatter);
        }
    }

    private BooleanExpression getOperador(DateTimeExpression<LocalDateTime> expression, String operador, LocalDateTime localDatetime) {
        if (operador.equals(EQ)) {
            return expression.eq(localDatetime);
        }
        if (operador.equals(GT)) {
            return expression.gt(localDatetime);
        }
        if (operador.equals(LT)) {
            return expression.lt(localDatetime);
        }
        if (operador.equals(GOE)) {
            return expression.goe(localDatetime);
        }
        if (operador.equals(LOE)) {
            return expression.loe(localDatetime);
        }

        return null;
    }

    private BooleanExpression getOperador(DateTimeExpression<LocalDateTime> expression, String operador, List<LocalDateTime> localDatetimes) {
        if (operador.equals(IN)) {
            return expression.in(localDatetimes);
        }
        if (operador.equals(NOT_IN)) {
            return expression.notIn(localDatetimes);
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof DateTimePath && expression.getType().equals(LocalDateTime.class);
    }
}
