package org.devocative.onfood.config;

import org.devocative.onfood.OnFoodException;
import org.devocative.onfood.dto.ErrorDTO;
import org.devocative.onfood.error.FoodErrorCode;
import org.devocative.onfood.error.RestaurateurErrorCode;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurateur;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class OnFoodControllerAdvice {
	private final Map<String, ErrorDTO.GeneralRs> uniqueConstraintErrors = new HashMap<>();

	@PostConstruct
	public void init() {
		uniqueConstraintErrors.put(Restaurateur.UC_MAIN.toLowerCase(),
			new ErrorDTO.GeneralRs(RestaurateurErrorCode.DuplicateCell)
				.addField("cell"));

		uniqueConstraintErrors.put(Food.UC_MAIN.toLowerCase(),
			new ErrorDTO.GeneralRs(FoodErrorCode.DuplicateName)
				.addField("name"));
	}

	@ExceptionHandler(OnFoodException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleOnFoodException(OnFoodException ex) {
		return new ResponseEntity<>(
			new ErrorDTO.GeneralRs(ex.getCode().getCode(), ex.getDescription()),
			HttpStatus.valueOf(ex.getCode().getHttpStatusCode())
		);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		final ConstraintViolationException cve = (ConstraintViolationException) ex.getCause();

		if (cve.getConstraintName() != null) {
			final String cn = cve.getConstraintName().toLowerCase();
			final Optional<ErrorDTO.GeneralRs> fieldOpt = uniqueConstraintErrors.entrySet().stream()
				.filter(e -> cn.contains(e.getKey()) || e.getKey().contains(cn))
				.map(Map.Entry::getValue)
				.findFirst();

			return new ResponseEntity<>(fieldOpt.orElse(new ErrorDTO.GeneralRs(cn, cn)), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(new ErrorDTO.GeneralRs(ex), HttpStatus.BAD_REQUEST);
	}

}
