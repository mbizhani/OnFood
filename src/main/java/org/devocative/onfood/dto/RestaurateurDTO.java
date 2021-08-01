package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public abstract class RestaurateurDTO {

	@Getter
	@Setter
	public static class RegisterRq {
		@NotEmpty
		private String firstName;
		@NotEmpty
		private String lastName;
		@NotEmpty
		private String cell;
		@NotEmpty
		private String code;
		@NotEmpty
		private String password;
	}


	@Getter
	@Setter
	@RequiredArgsConstructor
	public static class RegistrationCodeRs {
		private final String code;
	}
}
