package br.com.pavanati;

import br.com.pavanati.operatorstrategy.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RepresentationResolver {

    public static final Pattern PATTERN = Pattern.compile(" ?(and|or)? ?\\(? ?([A-Za-zÀ-ú0-9_.-]+) ?(!?[=><]{1,2}|in|nin) ?([_@'A-Za-zÀ-ú0-9!,. -]*)\\)?");
    public static final String EMPTY = "";
    public static final String EXCLA = "!";
    public static final String EQ = "=";
    public static final String NE = "!=";
    public static final String IN = "in";
    public static final String NOT_IN = "nin";
    public static final String GT = ">";
    public static final String LT = "<";
    public static final String GOE = ">=";
    public static final String LOE = "<=";
    public static final String CLEAN_PATTERN = "([ ]{0,1}'[ ]{0,1})";
    public static final String COMMA_PATTERN = "\\s*,\\s*";

    private RepresentationResolver() {
        throw new UnsupportedOperationException("RepresentationResolver should not be instantiated.");
    }

    private static final List<OperadorStrategy> STRATEGIES = Arrays.asList(
        new StringPathStrategy(),
        new UuidPathStrategy(),
        new LocalDatePathStrategy(),
        new LocalDateTimePathStrategy(),
        new OffsetDateTimePathStrategy(),
        new BooleanPathStrategy(),
        new EnumPathStrategy(),
        new NumberPathStrategy()
    );

    public static FilterArgument resolver(RepresentationBuilder representation, String filter) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (Objects.nonNull(filter) && !filter.isEmpty()) {
            filter = filter.replace("filter=", "");

            mountBooleanBuilder(representation, filter, booleanBuilder);
        }
        return new FilterArgument(new BooleanBuilder().and(booleanBuilder));
    }

    private static void mountBooleanBuilder(
            RepresentationBuilder representation,
            String filter,
            BooleanBuilder booleanBuilder) {
        Matcher matcher = PATTERN.matcher(filter);

        while (matcher.find()) {
            try {
                String operator = Objects.nonNull(matcher.group(1)) ? matcher.group(1) : "and";
                String parameter = matcher.group(2);
                String operador = matcher.group(3);
                String value = matcher.group(4).replaceAll("[()]", "");
                value = value.replace("@", " ").replaceAll("^ (.*)$", "$1").replaceAll("^(.*) $", "$1");
                ExpressionField expression1 = getExpression(representation, parameter);
                if (Objects.nonNull(expression1)) {
                    BooleanExpression expression = getOperadorByType(expression1.getExpression(), operador, value);
                    if (Objects.nonNull(expression)) {
                        if (operator.equals("or")) {
                            booleanBuilder.or(expression);
                        } else {
                            booleanBuilder.and(expression);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RepresentationException(e);
            }
        }
    }

    private static BooleanExpression getOperadorByType(Path<?> expression, String operador, String value) {
        return STRATEGIES.stream()
                .filter(strategy -> strategy.supports(expression))
                .findFirst()
                .map(strategy -> strategy.getOperador(expression, operador, value))
                .orElse(null);
    }

    private static ExpressionField getExpression(RepresentationBuilder representation, String parameter) {
        for (ExpressionField field : representation.getFields()) {
            if (field.getKey().equals(parameter)) {
                return field;
            }
        }

        return null;
    }

}
