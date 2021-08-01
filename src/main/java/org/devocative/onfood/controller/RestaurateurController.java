package org.devocative.onfood.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.iservice.IRestaurateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestaurateurController {
	private final IRestaurateurService personService;

	@GetMapping("/registrations/{cell}")
	public ResponseEntity<Void> sendRegistrationCode(@PathVariable String cell) {
		log.info("Start Registrations: cell=[{}]", cell);
		personService.sendRegistrationCode(cell);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/registrations")
	public ResponseEntity<Void> register(@Valid @RequestBody RestaurateurDTO.RegisterRq registerRq) {
		personService.register(registerRq);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//TODO JUST for DEV
	@GetMapping("/_dev_/registrations/{cell}")
	public ResponseEntity<RestaurateurDTO.RegistrationCodeRs> getRegistrationCode(@PathVariable String cell) {
		log.info("Start Registrations: cell=[{}]", cell);
		return ResponseEntity.ok(new RestaurateurDTO.RegistrationCodeRs(personService.getRegistrationCode(cell)));
	}
}
