package org.devocative.onfood.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.devocative.onfood.OnFoodException;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.error.RestaurateurErrorCode;
import org.devocative.onfood.iservice.IBeanMapper;
import org.devocative.onfood.iservice.IRestaurateurService;
import org.devocative.onfood.iservice.ISecurityService;
import org.devocative.onfood.model.Restaurateur;
import org.devocative.onfood.repository.IRestaurateurRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurateurService implements IRestaurateurService {
	private final IBeanMapper beanMapper;
	private final IRestaurateurRepository restaurateurRepository;
	private final ISecurityService securityService;

	// ---------------

	// TODO replace with Redis
	private final Map<String, String> sentRegistrationCodes =
		Collections.synchronizedMap(new PassiveExpiringMap<>(30_000));
	private final Random random = new Random();

	// ------------------------------

	@PreAuthorize("hasAuthority('" + ISecurityService.RESTAURATEUR_ROLE + "') and #id == authentication.credentials")
	@Override
	public RestaurateurDTO.RestaurateurRs getRestaurateur(Long id) {
		final Restaurateur restaurateur = restaurateurRepository
			.findById(id)
			.orElseThrow(() -> new OnFoodException(RestaurateurErrorCode.RestaurateurNotFound, id.toString()));

		return beanMapper.toRestaurateurRs(restaurateur);
	}

	@Override
	public void sendRegistrationCode(String cell) {
		sentRegistrationCodes.put(cell, String.format("%04d", random.nextInt(10000)));
		//TODO need send code via SMS
	}

	@Transactional
	@Override
	public RestaurateurDTO.RegisterRs register(RestaurateurDTO.RegisterRq registerRq) {
		if (registerRq.getCode().equals(sentRegistrationCodes.get(registerRq.getCell()))) {
			sentRegistrationCodes.remove(registerRq.getCell());

			final var restaurateur = beanMapper.toRestaurateur(registerRq);
			restaurateurRepository.saveAndFlush(restaurateur);
			log.info("Registered Restaurateur: {}", restaurateur);

			return new RestaurateurDTO.RegisterRs(restaurateur.getId(),
				securityService.generateToken(
					restaurateur.getCell(),
					restaurateur.getId(),
					ISecurityService.RESTAURATEUR_ROLE));
		} else {
			throw new OnFoodException(RestaurateurErrorCode.UnregisteredCellOrInvalidCode);
		}
	}

	// ---------------

	@Override
	public String getRegistrationCode(String cell) {
		return sentRegistrationCodes.get(cell);
	}
}
