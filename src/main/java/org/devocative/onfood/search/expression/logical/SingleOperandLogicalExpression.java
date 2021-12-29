package org.devocative.onfood.search.expression.logical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.ABooleanExpression;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class SingleOperandLogicalExpression extends ABooleanExpression<LogicalOperator.ESingleOperand> {
	private ABooleanExpression<?> operand;

	public SingleOperandLogicalExpression(LogicalOperator.ESingleOperand operator, ABooleanExpression<?> operand) {
		setOperator(operator);
		this.operand = operand;
	}
}
