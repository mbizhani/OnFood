package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public abstract class RestaurateurDTO {

	@Getter
	@Setter
	public static class RegisterRq {
		@NotNull
		private String firstName;
		@NotNull
		private String lastName;
		@NotNull
		private String cell;
		@NotNull
		private String code;
		@NotNull
		private String password;
	}


	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class RegistrationCodeRs {
		private final String code;
	}
}
