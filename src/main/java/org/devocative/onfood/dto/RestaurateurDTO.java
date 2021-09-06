package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class RestaurateurDTO {
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String cell;
	private String email;

	// ------------------------------

	@Getter
	@Setter
	public static class RegisterRq extends RestaurateurDTO {
		@NotNull
		private String code;
		@NotNull
		private String password;
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class RegisterRs {
		private final Long userId;
		private final String token;
	}

	@Getter
	@Setter
	public static class RestaurateurRs extends RestaurateurDTO {
		private Long id;
		private String createdBy;
		private CalendarDTO.DateDTO createdDate;
		private String lastModifiedBy;
		private CalendarDTO.DateDTO lastModifiedDate;
		private Integer version;
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class RegistrationCodeRs {
		private final String code;
	}
}
