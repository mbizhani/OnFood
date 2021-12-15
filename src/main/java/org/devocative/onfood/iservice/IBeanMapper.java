package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.model.Restaurateur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IBeanMapper extends IBaseBeanMapper {
	Restaurateur cloneFrom(Restaurateur src);

	Restaurateur toRestaurateur(RestaurateurDTO.RegisterRq registerRq);

	void updateRestaurateur(@MappingTarget Restaurateur dest, RestaurateurDTO.RestaurateurRq src);

	RestaurateurDTO.RestaurateurRs toRestaurateurRs(Restaurateur restaurateur);
}
