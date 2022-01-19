package org.devocative.onfood.search.processor.logical;

import org.devocative.onfood.search.Processors;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class MultiOperandLogicalProcessor implements IBooleanProcessor<MultiOperandLogicalExpression> {

	@Override
	public Predicate process(MultiOperandLogicalExpression expression, Root<?> root, CriteriaBuilder builder) {
		final Predicate[] predicates = expression.getOperands().stream()
			.map(expr -> Processors.process(expr, root, builder))
			.toArray(Predicate[]::new);

		switch (expression.getOperator()) {
			case And:
				return builder.and(predicates);
			case Or:
				return builder.or(predicates);
			default:
				throw new RuntimeException("Invalid LogicalOperator.EMultiOperand: " + expression.getOperator());
		}
	}
}
