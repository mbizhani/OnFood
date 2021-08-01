package org.devocative.onfood.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.OnFoodException;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.error.RestaurateurErrorCode;
import org.devocative.onfood.iservice.BeanMapper;
import org.devocative.onfood.iservice.IRestaurateurService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurateurService implements IRestaurateurService {
	private final BeanMapper beanMapper;

	// ---------------

	// TODO replace with Redis
	private final Map<String, String> sentRegistrationCodes = new ConcurrentHashMap<>();
	private final Random random = new Random();

	// ------------------------------

	@Override
	public void sendRegistrationCode(String cell) {
		sentRegistrationCodes.put(cell, String.format("%04d", random.nextInt(10000)));
		//TODO need send code via SMS
	}

	@Override
	public void register(RestaurateurDTO.RegisterRq registerRq) {
		if (registerRq.getCode().equals(sentRegistrationCodes.get(registerRq.getCell()))) {
			final var person = beanMapper.toRestaurateur(registerRq);
			log.info("Registering Person: {}", person);
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
