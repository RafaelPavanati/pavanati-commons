package br.com.pavanati;

import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class RepresentationResolverTest {

    @Mock
    private RepresentationBuilder representationBuilder;

    @Mock
    private ExpressionField expressionField;

    private String OFFSET_MATCH_PATTERN = "^(datetime .{1,2} 2023-09-20T10:00).*";

    private String OFFSET_MATCH_GROUP = "$1";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "", "filter=invalidFilter[", "filter=()"})
    void whenSentInvalidFilterShouldGenerateBooleanBuilderWithNullFilter(String values) {
        // Arrange
        String filter = Objects.nonNull(values) && !values.equals("null") ? values : null;

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=test=value", "filter=(test=value)", "filter=( test = value )"})
    void whenSentEqualsStringFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("test");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("test"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("test = value", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter=!like", "filter=(parameter=!like)", "filter=( parameter = !like )"})
    void whenSentEqualsStringLikeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("containsIc(parameter,like)", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter!=value", "filter=(parameter!=value)", "filter=( parameter != value )"})
    void whenSentNotEqualsStringLikeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("!(eqIc(parameter,value))", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=test in value,value2", "filter=( test in value,value2 )"})
    void whenSentInStringFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("test");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("test"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("test in [value, value2]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=test nin value,value2", "filter=( test nin value,value2 )"})
    void whenSentNotInStringFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("test");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("test"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("test not in [value, value2]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=boolean=true", "filter=(boolean=true)", "filter=( boolean = true )"})
    void whenSentBooleanTrueFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("boolean");
        when(expressionField.getExpression()).thenReturn(Expressions.booleanPath("boolean"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("boolean = true", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=boolean=false", "filter=(boolean=false)", "filter=( boolean = false )"})
    void whenSentBooleanFalseFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("boolean");
        when(expressionField.getExpression()).thenReturn(Expressions.booleanPath("boolean"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("boolean = false", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date=2023-09-20", "filter=(date=2023-09-20)", "filter=( date = 2023-09-20 )"})
    void whenSentEqualsDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date = 2023-09-20", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date>2023-09-20", "filter=(date>2023-09-20)", "filter=( date > 2023-09-20 )"})
    void whenSentGreaterDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date > 2023-09-20", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date<2023-09-20", "filter=(date<2023-09-20)", "filter=( date < 2023-09-20 )"})
    void whenSentLessDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date < 2023-09-20", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date>=2023-09-20", "filter=(date>=2023-09-20)", "filter=( date >= 2023-09-20 )"})
    void whenSentGreaterOrEqualsDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date >= 2023-09-20", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date<=2023-09-20", "filter=(date<=2023-09-20)", "filter=( date <= 2023-09-20 )"})
    void whenSentLessOrEqualsDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date <= 2023-09-20", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date in 2023-09-20,2023-09-21", "filter=( date in 2023-09-20,2023-09-21 )"})
    void whenSentInDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date in [2023-09-20, 2023-09-21]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=date nin 2023-09-20,2023-09-21", "filter=( date nin 2023-09-20,2023-09-21 )"})
    void whenSentNotInDateFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("date");
        when(expressionField.getExpression()).thenReturn(Expressions.datePath(LocalDate.class, "date"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("date not in [2023-09-20, 2023-09-21]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime=2023-09-20 10:15", "filter=(datetime=2023-09-20 10:15)", "filter=( datetime = 2023-09-20 10:15 )"})
    void whenSentEqualsLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime = 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime>2023-09-20 10:15", "filter=(datetime>2023-09-20 10:15)", "filter=( datetime > 2023-09-20 10:15 )"})
    void whenSentGreaterLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime > 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime<2023-09-20 10:15", "filter=(datetime<2023-09-20 10:15)", "filter=( datetime < 2023-09-20 10:15 )"})
    void whenSentLessLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime < 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime>=2023-09-20 10:15", "filter=(datetime>=2023-09-20 10:15)", "filter=( datetime >= 2023-09-20 10:15 )"})
    void whenSentGreaterOrEqualsLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime >= 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime<=2023-09-20 10:15", "filter=(datetime<=2023-09-20 10:15)", "filter=( datetime <= 2023-09-20 10:15 )"})
    void whenSentLessOrEqualsLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime <= 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime in 2023-09-20 10:15,2023-09-10 01:15", "filter=( datetime in 2023-09-20 10:15,2023-09-10 01:15 )"})
    void whenSentInLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) { // FIXME
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime = 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime nin 2023-09-20 10:15,2023-09-10 01:15", "filter=( datetime nin 2023-09-20 10:15,2023-09-10 01:15 )"})
    void whenSentNotInLocalDateTimeFilterShouldGenerateBooleanBuilder(String filter) { // FIXME
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(LocalDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime != 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime=2023-09-20 10:15", "filter=(datetime=2023-09-20 10:15)", "filter=( datetime = 2023-09-20 10:15 )"})
    void whenSentEqualsOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime = 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime>2023-09-20 10:15", "filter=(datetime>2023-09-20 10:15)", "filter=( datetime > 2023-09-20 10:15 )"})
    void whenSentGreaterOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime > 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime<2023-09-20 10:15", "filter=(datetime<2023-09-20 10:15)", "filter=( datetime < 2023-09-20 10:15 )"})
    void whenSentLessOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime < 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime>=2023-09-20 10:15", "filter=(datetime>=2023-09-20 10:15)", "filter=( datetime >= 2023-09-20 10:15 )"})
    void whenSentGreaterOrEqualsOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime >= 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime<=2023-09-20 10:15", "filter=(datetime<=2023-09-20 10:15)", "filter=( datetime <= 2023-09-20 10:15 )"})
    void whenSentLessOrEqualsOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime <= 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime in 2023-09-20 10:15,2023-09-10 01:15", "filter=( datetime in 2023-09-20 10:15,2023-09-10 01:15 )"})
    void whenSentInOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) { // FIXME
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime = 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=datetime nin 2023-09-20 10:15,2023-09-10 01:15", "filter=( datetime nin 2023-09-20 10:15,2023-09-10 01:15 )"})
    void whenSentNotInOffsetDateTimeFilterShouldGenerateBooleanBuilder(String filter) { // FIXME
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("datetime");
        when(expressionField.getExpression()).thenReturn(Expressions.dateTimePath(OffsetDateTime.class, "datetime"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("datetime != 2023-09-20T10:00", result.getBooleanBuilder().getValue().toString().replaceAll(OFFSET_MATCH_PATTERN, OFFSET_MATCH_GROUP));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "filter=parameter=550e8400-e29b-41d4-a716-446655440000",
            "filter=(parameter=550e8400-e29b-41d4-a716-446655440000)",
            "filter=( parameter = 550e8400-e29b-41d4-a716-446655440000 )"
    })
    void whenSentEqualsUuidFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.comparablePath(UUID.class, "parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("parameter = 550e8400-e29b-41d4-a716-446655440000", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "filter=parameter in 550e8400-e29b-41d4-a716-446655440000,550e8400-e29b-41d4-a716-446655440001",
            "filter=( parameter in 550e8400-e29b-41d4-a716-446655440000,550e8400-e29b-41d4-a716-446655440001 )"
    })
    void whenSentInUuidFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.path(UUID.class, "parameter"));


        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertEquals("parameter in [550e8400-e29b-41d4-a716-446655440000, 550e8400-e29b-41d4-a716-446655440001]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "filter=parameter nin 550e8400-e29b-41d4-a716-446655440000,550e8400-e29b-41d4-a716-446655440001",
            "filter=( parameter nin 550e8400-e29b-41d4-a716-446655440000,550e8400-e29b-41d4-a716-446655440001 )"
    })
    void whenSentNotInUuidFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.path(UUID.class, "parameter"));


        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertEquals("parameter not in [550e8400-e29b-41d4-a716-446655440000, 550e8400-e29b-41d4-a716-446655440001]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter=SENT", "filter=(parameter=SENT)", "filter=( parameter = SENT )"})
    void whenSentEqualsEnumFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.enumPath(EEnum.class, "parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("parameter = SENT", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter=123.45", "filter=(parameter=123.45)", "filter=( parameter = 123.45 )"})
    void whenSentEqualsNumberFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.numberPath(BigDecimal.class, "parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("parameter = 123.45", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter in 123.45,432.12", "filter=( parameter in 123.45,432.12 )"})
    void whenSentInNumberFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.numberPath(BigDecimal.class, "parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("parameter in [123.45, 432.12]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=parameter nin 123.45,432.12", "filter=( parameter nin 123.45,432.12 )"})
    void whenSentNotInNumberFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("parameter");
        when(expressionField.getExpression()).thenReturn(Expressions.numberPath(BigDecimal.class, "parameter"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("parameter not in [123.45, 432.12]", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=(test=value)or(test=value2)", "filter=( test = value ) or ( test = value2 )"})
    void whenSentEqualsStringWithParenthesisAndOrClauseFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("test");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("test"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("test = value || test = value2", result.getBooleanBuilder().getValue().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"filter=(test=value)and(test=value2)", "filter=( test = value ) and ( test = value2 )"})
    void whenSentEqualsStringWithParenthesisAndAndClauseFilterShouldGenerateBooleanBuilder(String filter) {
        // Arrange
        when(representationBuilder.getFields()).thenReturn(List.of(expressionField));
        when(expressionField.getKey()).thenReturn("test");
        when(expressionField.getExpression()).thenReturn(Expressions.stringPath("test"));

        // Act
        FilterArgument result = RepresentationResolver.resolver(representationBuilder, filter);

        // Assert
        assertNotNull(result);
        assertEquals("test = value && test = value2", result.getBooleanBuilder().getValue().toString());
    }

}

