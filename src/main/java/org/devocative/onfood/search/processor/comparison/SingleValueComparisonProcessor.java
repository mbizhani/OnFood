package org.devocative.onfood.search.processor.comparison;

import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.devocative.onfood.search.ProcessorUtil.convertSingleValue;
import static org.devocative.onfood.search.ProcessorUtil.findPath;

public class SingleValueComparisonProcessor implements IBooleanProcessor<SingleValueComparisonExpression> {

	@Override
	public Predicate process(SingleValueComparisonExpression expression, Root<?> root, CriteriaBuilder builder) {
		final Path path = findPath(root, expression.getProperty());
		final Comparable value = convertSingleValue(expression.getValue(), path);

		switch (expression.getOperator()) {
			case Equal:
				return builder.equal(path, value);
			case NotEqual:
				return builder.not(builder.equal(path, value));
			case LessThan:
				return builder.lessThan(path, value);
			case LessThanEqual:
				return builder.lessThanOrEqualTo(path, value);
			case GreaterThan:
				return builder.greaterThan(path, value);
			case GreaterThanEqual:
				return builder.greaterThanOrEqualTo(path, value);
			case StartWith:
				return builder.like(path, expression.getValue() + "%");
			case EndWith:
				return builder.like(path, "%" + expression.getValue());
			case Contain:
				return builder.like(path, "%" + expression.getValue() + "%");
			default:
				throw new RuntimeException("Invalid ComparisonOperator.ESingleValue: " + expression.getOperator());
		}
	}

}
