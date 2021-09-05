package org.devocative.onfood.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RestaurateurErrorCode implements IErrorCode {
	RestaurateurNotFound(404),
	UnregisteredCellOrInvalidCode(400),
	DuplicateCell(400);

	// ------------------------------

	private final Integer httpStatusCode;

	@Override
	public String getName() {
		return name();
	}
}
