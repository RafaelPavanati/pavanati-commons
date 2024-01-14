package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.DateTimePath;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static br.com.pavanati.RepresentationResolver.*;

public class OffsetDateTimePathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        DateTimeExpression<OffsetDateTime> expression = (DateTimeExpression) exp;
        if (operador.equals(IN) || operador.equals(NOT_IN)) {
            List<OffsetDateTime> offsetDateTimes = Arrays.stream(value.split(COMMA_PATTERN)).map(this::parse).toList();
            return getOperador(expression, operador, offsetDateTimes);
        }

        return getOperador(expression, operador, parse(value));
    }

    private OffsetDateTime parse(String value) {
        ZoneId zoneId = ZoneId.systemDefault();
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime localDateTime = LocalDate.parse(value, formatter).atStartOfDay();

            return ZonedDateTime.of(localDateTime, zoneId).toOffsetDateTime();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm][yyyy-MM-dd HH][yyyy-MM-dd]");
            LocalDateTime localDateTime = LocalDateTime.parse(value.replaceAll("[ T]", " "), formatter);

            return ZonedDateTime.of(localDateTime, zoneId).toOffsetDateTime();
        }
    }

    private BooleanExpression getOperador(DateTimeExpression<OffsetDateTime> expression, String operador, OffsetDateTime offsetDateTime) {
        if (operador.equals(EQ)) {
            return expression.eq(offsetDateTime);
        }
        if (operador.equals(GT)) {
            return expression.gt(offsetDateTime);
        }
        if (operador.equals(LT)) {
            return expression.lt(offsetDateTime);
        }
        if (operador.equals(GOE)) {
            return expression.goe(offsetDateTime);
        }
        if (operador.equals(LOE)) {
            return expression.loe(offsetDateTime);
        }

        return null;
    }

    private BooleanExpression getOperador(DateTimeExpression<OffsetDateTime> expression, String operador, List<OffsetDateTime> offsetDateTimes) {
        if (operador.equals(IN)) {
            return expression.in(offsetDateTimes);
        }
        if (operador.equals(NOT_IN)) {
            return expression.notIn(offsetDateTimes);
        }

        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof DateTimePath && expression.getType().equals(OffsetDateTime.class);
    }
}
