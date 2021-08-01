package org.devocative.onfood.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.iservice.IRestaurateurService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Profile("!production")
@RestController
@RequestMapping("/api/j4d")
public class Just4DevController {
	private final IRestaurateurService restaurateurService;

	@GetMapping("/registrations/{cell}")
	public ResponseEntity<RestaurateurDTO.RegistrationCodeRs> getRegistrationCode(@PathVariable String cell) {
		log.warn("Get Registrations Code: cell=[{}]", cell);
		return ResponseEntity.ok(new RestaurateurDTO.RegistrationCodeRs(restaurateurService.getRegistrationCode(cell)));
	}
}
