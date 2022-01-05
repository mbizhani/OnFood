package org.devocative.onfood.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortExpression {
	public enum EMode {
		Asc, Desc
	}

	@NotBlank
	private String property;

	@NotNull
	private EMode mode = EMode.Asc;

	// ------------------------------

	public SortExpression(String property) {
		this.property = property;
	}
}
