package br.com.pavanati.operatorstrategy;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;

public class BooleanPathStrategy implements OperadorStrategy {
    @Override
    public BooleanExpression getOperador(Path exp, String operador, String value) {
        BooleanPath expression = (BooleanPath) exp;
        if (value.equals("true")) {
            return expression.isTrue();
        }
        if (value.equals("false")) {
            return expression.isFalse();
        }
        return null;
    }

    @Override
    public boolean supports(Path expression) {
        return expression instanceof BooleanPath;
    }
}
