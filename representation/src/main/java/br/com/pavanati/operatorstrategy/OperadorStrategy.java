package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface OperadorStrategy {

    BooleanExpression getOperador(Path<?> expression, String operador, String value);

    boolean supports(Path<?> expression);

}
