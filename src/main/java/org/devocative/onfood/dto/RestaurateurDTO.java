package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class RestaurateurDTO {
	/*
	it is @NotNull, but just for test, only applied on the entity.
	*/
	private String firstName;
	/*
	it is @NotNull, but just for test, only applied on the entity.
	*/
	private String lastName;
	@NotNull
	private String cell;
	private String email;
	private CalendarDTO.DateDTO birthDate;

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
		private CalendarDTO.DateTimeDTO createdDate;
		private String lastModifiedBy;
		private CalendarDTO.DateTimeDTO lastModifiedDate;
		private Integer version;
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class RegistrationCodeRs {
		private final String code;
	}

	@Getter
	@Setter
	public static class LoginRq {
		@NotNull
		private String cell;
		@NotNull
		private String password;
	}

	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class LoginRs {
		private final Long userId;
		private final String token;
	}

}
