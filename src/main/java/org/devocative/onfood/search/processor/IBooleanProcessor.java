package org.devocative.onfood.search.processor;

import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface IBooleanProcessor<B extends ABooleanExpression<?>> {
	Predicate process(B expression, Root<?> root, CriteriaBuilder builder);
}
