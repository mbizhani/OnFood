package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.RestaurateurDTO;

public interface IRestaurateurService {
	void sendRegistrationCode(String cell);

	void register(RestaurateurDTO.RegisterRq registerRq);

	String getRegistrationCode(String cell);
}
