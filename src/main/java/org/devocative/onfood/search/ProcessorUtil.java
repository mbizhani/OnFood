package org.devocative.onfood.search;

import org.devocative.onfood.search.expression.ABooleanExpression;
import org.devocative.onfood.search.expression.comparison.MultiValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.NoValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;
import org.devocative.onfood.search.expression.logical.SingleOperandLogicalExpression;
import org.devocative.onfood.search.processor.IBooleanProcessor;
import org.devocative.onfood.search.processor.comparison.MultiValueComparisonProcessor;
import org.devocative.onfood.search.processor.comparison.NoValueComparisonProcessor;
import org.devocative.onfood.search.processor.comparison.RangeValueComparisonProcessor;
import org.devocative.onfood.search.processor.comparison.SingleValueComparisonProcessor;
import org.devocative.onfood.search.processor.logical.MultiOperandLogicalProcessor;
import org.devocative.onfood.search.processor.logical.SingleOperandLogicalProcessor;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class ProcessorUtil {
	private static final Map<Class<? extends ABooleanExpression<?>>, IBooleanProcessor> PROCESSORS =
		new ConcurrentHashMap<>();

	static {
		PROCESSORS.put(MultiOperandLogicalExpression.class, new MultiOperandLogicalProcessor());
		PROCESSORS.put(SingleOperandLogicalExpression.class, new SingleOperandLogicalProcessor());

		PROCESSORS.put(MultiValueComparisonExpression.class, new MultiValueComparisonProcessor());
		PROCESSORS.put(RangeValueComparisonExpression.class, new RangeValueComparisonProcessor());
		PROCESSORS.put(SingleValueComparisonExpression.class, new SingleValueComparisonProcessor());
		PROCESSORS.put(NoValueComparisonExpression.class, new NoValueComparisonProcessor());
	}

	public static Predicate process(ABooleanExpression<?> expression, Root<?> root, CriteriaBuilder builder) {
		return PROCESSORS.get(expression.getClass()).process(expression, root, builder);
	}

	public static Path<?> findPath(Root<?> root, String property) {
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

	public static List<Comparable<?>> convertListValue(List<String> values, Path<? extends Comparable<?>> path) {
		return values.stream()
			.filter(Objects::nonNull)
			.map(v -> convert(path.getJavaType(), v))
			.collect(Collectors.toList());
	}

	public static Comparable convertSingleValue(String value, Path<?> path) {
		return convert(path.getJavaType(), value);
	}

	private static Comparable<?> convert(Class<?> cls, String value) {
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

		if (cls.equals(LocalTime.class)) {
			return LocalTime.parse(value);
		}

		return value;
	}
}
