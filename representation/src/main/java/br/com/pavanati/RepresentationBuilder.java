package br.com.pavanati;


import com.querydsl.core.types.Path;

import java.util.ArrayList;
import java.util.List;

public class RepresentationBuilder {

    List<ExpressionField> fields = new ArrayList<>();

    public List<ExpressionField> getFields() {
        return fields;
    }

    public RepresentationBuilder add(String field, Path<?> expression) {
        this.fields.add(new ExpressionField(field, expression, false));
        return this;
    }


    public RepresentationBuilder add(String field, Path<?> expression, boolean sortable) {
        this.fields.add(new ExpressionField(field, expression, sortable));
        return this;
    }

}
