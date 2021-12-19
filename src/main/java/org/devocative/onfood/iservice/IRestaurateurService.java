package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.RestaurateurDTO;

public interface IRestaurateurService {
	RestaurateurDTO.RestaurateurRs getRestaurateur(Long id);

	void sendRegistrationCode(String cell);

	RestaurateurDTO.RegisterRs register(RestaurateurDTO.RegisterRq registerRq);

	RestaurateurDTO.RestaurateurRs update(Long id, RestaurateurDTO.RestaurateurRq restaurateurRq);

	RestaurateurDTO.LoginRs login(RestaurateurDTO.LoginRq loginRq);

	String getRegistrationCode(String cell);
}
