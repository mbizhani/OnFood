package org.devocative.onfood.search.expression.logical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class SingleOperandLogicalExpression extends ABooleanExpression<LogicalOperator.ESingleOperand> {
	@Valid
	@NotNull
	private ABooleanExpression<?> operand;

	public SingleOperandLogicalExpression(LogicalOperator.ESingleOperand operator, ABooleanExpression<?> operand) {
		setOperator(operator);
		this.operand = operand;
	}

	@Override
	public void validate() {
		super.validate();

		if (operand == null) {
			throw new RuntimeException("Operand Required: " + this);
		}
	}
}
