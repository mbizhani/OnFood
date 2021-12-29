package org.devocative.onfood.search.expression;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.comparison.MultiValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.NoValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,
	property = "operator",
	include = JsonTypeInfo.As.EXISTING_PROPERTY,
	visible = true,
	defaultImpl = SingleValueComparisonExpression.class)
@JsonSubTypes({
	@JsonSubTypes.Type(names = {"And", "Or"}, value = MultiOperandLogicalExpression.class),
	@JsonSubTypes.Type(names = {"Empty", "NotEmpty"}, value = NoValueComparisonExpression.class),
	@JsonSubTypes.Type(names = {"Between"}, value = RangeValueComparisonExpression.class),
	@JsonSubTypes.Type(names = {"In", "NotIn"}, value = MultiValueComparisonExpression.class)
})
@Getter
@Setter
@ToString
public abstract class ABooleanExpression<T> {
	@NotNull
	private T operator;
}
