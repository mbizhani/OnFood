package org.devocative.onfood.search.processor.comparison;

import org.devocative.onfood.search.expression.comparison.NoValueComparisonExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.devocative.onfood.search.ProcessorUtil.findPath;

public class NoValueComparisonProcessor implements IBooleanProcessor<NoValueComparisonExpression> {

	@Override
	public Predicate process(NoValueComparisonExpression expression, Root<?> root, CriteriaBuilder builder) {
		final Path path = findPath(root, expression.getProperty());
		switch (expression.getOperator()) {
			case Empty:
				return path.isNull();
			case NotEmpty:
				return path.isNotNull();
			default:
				throw new RuntimeException("Invalid ComparisonOperator.ENoValue: " + expression.getOperator());
		}
	}
}
