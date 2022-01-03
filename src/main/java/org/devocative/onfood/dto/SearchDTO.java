package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.devocative.onfood.search.SortExpression;
import org.devocative.onfood.search.expression.ABooleanExpression;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public abstract class SearchDTO {

	@Getter
	@Setter
	@Accessors(chain = true)
	public static class SearchRq {
		@NotNull
		@Min(0)
		private Integer startIndex;

		@NotNull
		@Min(1)
		private Integer count;

		private Boolean distinct = Boolean.FALSE;
		private ABooleanExpression<?> filter;
		private List<SortExpression> sorts;
	}

	@Getter
	@RequiredArgsConstructor
	public static class SearchRs<D> {
		private final List<D> result;
		private final Long totalCount;
	}
}
