package org.devocative.onfood.config;

import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.OnFoodException;
import org.devocative.onfood.dto.ErrorDTO;
import org.devocative.onfood.error.FoodErrorCode;
import org.devocative.onfood.error.RestaurateurErrorCode;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurateur;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
//@ControllerAdvice
public class OnFoodControllerAdvice {
	private final Map<String, ErrorDTO.GeneralRs> uniqueConstraintErrors = new HashMap<>();

	@PostConstruct
	public void init() {
		uniqueConstraintErrors.put(Restaurateur.UC_MAIN.toLowerCase(),
			new ErrorDTO.GeneralRs(RestaurateurErrorCode.DuplicateCell)
				.addField(new ErrorDTO.FieldDTO("cell")));

		uniqueConstraintErrors.put(Food.UC_MAIN.toLowerCase(),
			new ErrorDTO.GeneralRs(FoodErrorCode.DuplicateName)
				.addField(new ErrorDTO.FieldDTO("name")));
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
		if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
			final org.hibernate.exception.ConstraintViolationException cve =
				(org.hibernate.exception.ConstraintViolationException) ex.getCause();

			if (cve.getConstraintName() != null) {
				final String cn = cve.getConstraintName().toLowerCase();
				final Optional<ErrorDTO.GeneralRs> fieldOpt = uniqueConstraintErrors.entrySet().stream()
					.filter(e -> cn.contains(e.getKey()) || e.getKey().contains(cn))
					.map(Map.Entry::getValue)
					.findFirst();

				return new ResponseEntity<>(fieldOpt.orElse(new ErrorDTO.GeneralRs(cn, cn)), HttpStatus.BAD_REQUEST);
			} else {
				log.warn("@ControllerAdvice - ConstraintViolationException without ConstraintName:", cve);
			}
		} else {
			log.warn("@ControllerAdvice - DataIntegrityViolationException Unknown Cause: ", ex);
		}

		return new ResponseEntity<>(new ErrorDTO.GeneralRs("DataIntegrityViolation"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleSecurityException(AccessDeniedException ex) {
		return new ResponseEntity<>(new ErrorDTO.GeneralRs("AccessDenied"), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		final List<ErrorDTO.FieldDTO> errorFieldDTOS = ex
			.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(fieldError -> new ErrorDTO.FieldDTO(fieldError.getField(), fieldError.getCode()))
			.collect(Collectors.toList());

		ex.getBindingResult()
			.getGlobalErrors()
			.stream()
			.map(objectError -> new ErrorDTO.FieldDTO(null, objectError.getCode()))
			.collect(Collectors.toCollection(() -> errorFieldDTOS));

		return new ResponseEntity<>(
			new ErrorDTO.GeneralRs("InputValidationError").addFields(errorFieldDTOS), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleConstraintViolationException(ConstraintViolationException ex) {
		final List<ErrorDTO.FieldDTO> fields = ex.getConstraintViolations().stream()
			.map(cv -> new ErrorDTO.FieldDTO(
				cv.getPropertyPath().toString(),
				cv.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()))
			.collect(Collectors.toList());

		return new ResponseEntity<>(
			new ErrorDTO.GeneralRs("InputValidationError").addFields(fields), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO.GeneralRs> handleException(Exception ex) {
		log.error("@ControllerAdvice - General Exception:", ex);
		return new ResponseEntity<>(new ErrorDTO.GeneralRs("Unknown"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
