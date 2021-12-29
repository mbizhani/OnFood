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
public class NoValueComparisonExpression extends ABooleanExpression<ComparisonOperator.ENoValue> {
	@NotEmpty
	private String property;

	public NoValueComparisonExpression(ComparisonOperator.ENoValue operator, String property) {
		setOperator(operator);
		this.property = property;
	}
}
