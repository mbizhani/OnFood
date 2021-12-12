package org.devocative.onfood.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.devocative.onfood.error.IErrorCode;

import java.util.ArrayList;
import java.util.List;

public abstract class ErrorDTO {

	@Getter
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class GeneralRs {
		private final String code;
		private final String description;
		private final List<String> fields = new ArrayList<>();

		// ------------------------------

		public GeneralRs(Exception e) {
			this(e.getClass().getSimpleName(), e.getMessage());
		}

		public GeneralRs(IErrorCode errorCode) {
			this(errorCode.getCode(), errorCode.getCode());
		}

		public GeneralRs(IErrorCode errorCode, String description) {
			this(errorCode.getCode(), description);
		}

		public GeneralRs addField(String field) {
			fields.add(field);
			return this;
		}
	}
}
