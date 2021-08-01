package org.devocative.onfood;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.devocative.onfood.error.IErrorCode;

@Getter
@RequiredArgsConstructor
public class OnFoodException extends RuntimeException {
	private final IErrorCode code;
	private final String description;

	// ------------------------------

	public OnFoodException(IErrorCode code) {
		this(code, null);
	}
}
