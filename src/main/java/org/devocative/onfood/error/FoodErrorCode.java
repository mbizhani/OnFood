package org.devocative.onfood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodErrorCode implements IErrorCode {
	DuplicateName(400);

	// ------------------------------

	private final Integer httpStatusCode;

	@Override
	public String getName() {
		return name();
	}
}
