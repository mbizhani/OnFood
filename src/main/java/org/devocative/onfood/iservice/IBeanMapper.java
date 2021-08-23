package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.model.Restaurateur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBeanMapper {
	Restaurateur toRestaurateur(RestaurateurDTO.RegisterRq registerRq);
}
