package org.devocative.onfood.search.processor.logical;

import org.devocative.onfood.search.Processors;
import org.devocative.onfood.search.expression.logical.SingleOperandLogicalExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SingleOperandLogicalProcessor implements IBooleanProcessor<SingleOperandLogicalExpression> {

	@Override
	public Predicate process(SingleOperandLogicalExpression expression, Root<?> root, CriteriaBuilder builder) {
		switch (expression.getOperator()) {
			case Not:
				return builder.not(Processors.process(expression.getOperand(), root, builder));
			default:
				throw new RuntimeException("Invalid LogicalOperator.ESingleOperand: " + expression.getOperator());
		}
	}
}
