package org.devocative.onfood.search.expression.comparison;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class NoValueComparisonExpression extends ABooleanExpression<ComparisonOperator.ENoValue> {
	@NotBlank
	private String property;

	public NoValueComparisonExpression(ComparisonOperator.ENoValue operator, String property) {
		setOperator(operator);
		this.property = property;
	}

	@Override
	public void validate() {
		if (isEmpty(property)) {
			throw new RuntimeException("Property Required: " + this);
		}
	}
}
