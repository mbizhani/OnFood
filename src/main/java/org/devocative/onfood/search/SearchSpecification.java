package org.devocative.onfood.search;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.search.expression.ABooleanExpression;
import org.devocative.onfood.search.expression.comparison.MultiValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.NoValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;
import org.devocative.onfood.search.expression.logical.SingleOperandLogicalExpression;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SearchSpecification<T> implements Specification<T> {
	private final ABooleanExpression<?> expression;

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		return processA(expression, root, builder);
	}

	private Predicate processA(ABooleanExpression<?> expression, Root<T> root, CriteriaBuilder builder) {
		expression.validate();

		if (expression instanceof MultiOperandLogicalExpression) {
			return process((MultiOperandLogicalExpression) expression, root, builder);
		} else if (expression instanceof SingleOperandLogicalExpression) {
			return process((SingleOperandLogicalExpression) expression, root, builder);
		} else if (expression instanceof MultiValueComparisonExpression) {
			return process((MultiValueComparisonExpression) expression, root, builder);
		} else if (expression instanceof RangeValueComparisonExpression) {
			return process((RangeValueComparisonExpression) expression, root, builder);
		} else if (expression instanceof SingleValueComparisonExpression) {
			return process((SingleValueComparisonExpression) expression, root, builder);
		} else if (expression instanceof NoValueComparisonExpression) {
			return process((NoValueComparisonExpression) expression, root, builder);
		}
		throw new RuntimeException("Invalid Expression");
	}

	private Predicate process(MultiOperandLogicalExpression expression, Root<T> root, CriteriaBuilder builder) {
		final List<Predicate> predicates = expression.getOperands().stream()
			.map(expr -> processA(expr, root, builder))
			.collect(Collectors.toList());
		switch (expression.getOperator()) {
			case And:
				return builder.and(predicates.toArray(new Predicate[0]));
			case Or:
				return builder.or(predicates.toArray(new Predicate[0]));
			default:
				throw new RuntimeException("Invalid LogicalOperator.EMultiOperand: " + expression.getOperator());
		}
	}

	private Predicate process(SingleOperandLogicalExpression expression, Root<T> root, CriteriaBuilder builder) {
		switch (expression.getOperator()) {
			case Not:
				return builder.not(processA(expression.getOperand(), root, builder));
			default:
				throw new RuntimeException("Invalid LogicalOperator.ESingleOperand: " + expression.getOperator());
		}
	}

	private Predicate process(MultiValueComparisonExpression expression, Root<T> root, CriteriaBuilder builder) {
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

	private Predicate process(RangeValueComparisonExpression expression, Root<T> root, CriteriaBuilder builder) {
		final Path path = findPath(root, expression.getProperty());

		switch (expression.getOperator()) {
			case Between:
				return builder.between(path,
					convertSingleValue(expression.getStart(), path),
					convertSingleValue(expression.getEnd(), path));
			default:
				throw new RuntimeException("Invalid ComparisonOperator.ERangeValue: " + expression.getOperator());
		}
	}

	private Predicate process(SingleValueComparisonExpression expression, Root<T> root, CriteriaBuilder builder) {
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

	private Predicate process(NoValueComparisonExpression expression, Root<T> root, CriteriaBuilder builder) {
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

	// ---------------

	private Path<?> findPath(Root<T> root, String property) {
		final String[] split = property.split("\\.");

		Path<?> path = root;
		for (String part : split) {
			if (Collection.class.isAssignableFrom(path.get(part).getJavaType())) {
				path = getOrCreateJoin(root, part);
			} else {
				path = path.get(part);
			}
		}

		if (path == null) {
			throw new RuntimeException("Invalid property: " + property + ", " + split[0]);
		}

		return path;
	}

	private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String property) {
		return from.join(property, JoinType.LEFT);
	}

	// ---------------

	private List<Comparable<?>> convertListValue(List<String> values, Path<? extends Comparable<?>> path) {
		return values.stream()
			.filter(Objects::nonNull)
			.map(v -> convert(path.getJavaType(), v))
			.collect(Collectors.toList());
	}

	private Comparable convertSingleValue(String value, Path<?> path) {
		return convert(path.getJavaType(), value);
	}

	private Comparable<?> convert(Class<?> cls, String value) {
		/*if (cls.equals(Short.class) || cls.equals(Integer.class) || cls.equals(Long.class) || cls.equals(Float.class) || cls.equals(Double.class)) {
			if (isNumeric(value))
				return value;
			else
				return null;
		}*/

		if (cls.equals(String.class)) {
			return value.trim();
		}

		if (cls.equals(BigDecimal.class)) {
			return new BigDecimal(value);
		}

		if (cls.equals(Boolean.class)) {
			return Boolean.valueOf(value);
		}

		if (cls.isEnum()) {
			for (Object constant : cls.getEnumConstants()) {
				if (constant.toString().equals(value)) {
					return (Comparable<?>) constant;
				}
			}
		}

		if (cls.equals(LocalDate.class)) {
			return LocalDate.parse(value);
		}

		if (cls.equals(LocalDateTime.class)) {
			return LocalDateTime.parse(value);
		}

		if (cls.equals(Instant.class)) {
			return Instant.parse(value);
		}

		return value;
	}

	/*private boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}*/

}