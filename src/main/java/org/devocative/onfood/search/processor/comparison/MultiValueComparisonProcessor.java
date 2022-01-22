package org.devocative.onfood.search.processor.comparison;

import org.devocative.onfood.search.expression.comparison.MultiValueComparisonExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.devocative.onfood.search.ProcessorUtil.convertListValue;
import static org.devocative.onfood.search.ProcessorUtil.findPath;

public class MultiValueComparisonProcessor implements IBooleanProcessor<MultiValueComparisonExpression> {

	@Override
	public Predicate process(MultiValueComparisonExpression expression, Root<?> root, CriteriaBuilder builder) {
		final Path path = findPath(root, expression.getProperty());
		final List values = convertListValue(expression.getValues(), path);

		switch (expression.getOperator()) {
			case In:
				return path.in(values);
			case NotIn:
				return builder.not(path.in(values));
			default:
				throw new RuntimeException("Invalid ComparisonOperator.EMultiValue: " + expression.getOperator());
		}
	}
}
