package org.devocative.onfood.search;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.search.expression.ABooleanExpression;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.devocative.onfood.search.SortExpression.EMode.Asc;

@RequiredArgsConstructor
public class SearchSpecification<T> implements Specification<T> {
	private final ABooleanExpression<?> expression;
	private final List<SortExpression> sorts;
	private final boolean distinct;

	// ------------------------------

	public SearchSpecification(ABooleanExpression<?> expression) {
		this(expression, null, false);
	}

	public SearchSpecification(ABooleanExpression<?> expression, List<SortExpression> sorts) {
		this(expression, sorts, false);
	}

	// ------------------------------

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (sorts != null && !sorts.isEmpty()) {
			final List<Order> orders = sorts.stream()
				.map(sort -> sort.getMode() == Asc ?
					builder.asc(Processors.findPath(root, sort.getProperty())) :
					builder.desc(Processors.findPath(root, sort.getProperty())))
				.collect(Collectors.toList());
			query.orderBy(orders);
		}

		query.distinct(distinct);

		return expression != null ? Processors.process(expression, root, builder) : null;
	}
}