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
public class RangeValueComparisonExpression extends ABooleanExpression<ComparisonOperator.ERangeValue> {
	@NotEmpty
	private String property;
	@NotEmpty
	private String start;
	@NotEmpty
	private String end;

	public RangeValueComparisonExpression(ComparisonOperator.ERangeValue operator, String property, String start, String end) {
		setOperator(operator);
		this.property = property;
		this.start = start;
		this.end = end;
	}
}
