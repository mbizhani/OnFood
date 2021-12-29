package org.devocative.onfood.search.expression.comparison;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class SingleValueComparisonExpression extends ABooleanExpression<ComparisonOperator.ESingleValue> {
	@NotEmpty
	private String property;
	@NotEmpty
	private String value;

	public SingleValueComparisonExpression(ComparisonOperator.ESingleValue operator, String property, String value) {
		setOperator(operator);
		this.property = property;
		this.value = value;
	}
}
