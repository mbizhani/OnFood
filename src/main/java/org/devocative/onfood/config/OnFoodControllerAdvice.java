package org.devocative.onfood.config;

import org.devocative.onfood.OnFoodException;
import org.devocative.onfood.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OnFoodControllerAdvice {

	@ExceptionHandler(OnFoodException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleOnFoodException(OnFoodException ex) {
		return new ResponseEntity<>(
			new ErrorDTO.GeneralRs(ex.getCode().getName(), ex.getDescription()),
			HttpStatus.valueOf(ex.getCode().getHttpStatusCode())
		);
	}

}
