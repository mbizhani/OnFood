package org.devocative.onfood.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class ErrorDTO {

	@Getter
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class GeneralRs {
		private final String code;
		private final String description;
	}
}
