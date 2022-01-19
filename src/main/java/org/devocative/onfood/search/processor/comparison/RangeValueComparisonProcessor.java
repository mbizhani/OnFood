package org.devocative.onfood.search.processor.comparison;

import org.devocative.onfood.search.Processors;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class RangeValueComparisonProcessor implements IBooleanProcessor<RangeValueComparisonExpression> {

	@Override
	public Predicate process(RangeValueComparisonExpression expression, Root<?> root, CriteriaBuilder builder) {
		final Path path = Processors.findPath(root, expression.getProperty());

		switch (expression.getOperator()) {
			case Between:
				return builder.between(path,
					Processors.convertSingleValue(expression.getStart(), path),
					Processors.convertSingleValue(expression.getEnd(), path));
			default:
				throw new RuntimeException("Invalid ComparisonOperator.ERangeValue: " + expression.getOperator());
		}

	}
}
