package org.devocative.onfood.search.expression.logical;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class MultiOperandLogicalExpression extends ABooleanExpression<LogicalOperator.EMultiOperand> {
	@Valid
	@NotNull
	@Size(min = 2)
	private List<ABooleanExpression<?>> operands;

	public MultiOperandLogicalExpression(LogicalOperator.EMultiOperand operator, ABooleanExpression<?>... operands) {
		setOperator(operator);
		this.operands = Arrays.asList(operands);
	}

	@Override
	public void validate() {
		super.validate();

		if (operands == null || operands.size() < 2) {
			throw new RuntimeException("More Than One Operand Required: " + this);
		}
	}
}
