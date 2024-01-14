package br.com.pavanati;

import com.querydsl.core.types.Path;
import lombok.Getter;

@Getter
public class ExpressionField {
    private final String key;
    private final Path expression;
    private boolean sortable = false;

    public String getKey() {
        return key;
    }

    public ExpressionField(String key, Path<?> expression, boolean sortable) {
        this.key = key;
        this.expression = expression;
        this.sortable = sortable;
    }
}
