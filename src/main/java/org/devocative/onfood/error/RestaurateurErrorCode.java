package org.devocative.onfood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RestaurateurErrorCode implements IErrorCode {
	UnregisteredCellOrInvalidCode(400);

	// ------------------------------

	private final Integer httpStatusCode;

	@Override
	public String getName() {
		return name();
	}
}
