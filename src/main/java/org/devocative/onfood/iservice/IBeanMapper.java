package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.FoodDTO;
import org.devocative.onfood.dto.RestaurantDTO;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurant;
import org.devocative.onfood.model.Restaurateur;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IBeanMapper extends IBaseBeanMapper {
	Restaurateur cloneFrom(Restaurateur src);

	Restaurateur toRestaurateur(RestaurateurDTO.RegisterRq registerRq);

	void updateRestaurateur(@MappingTarget Restaurateur dest, RestaurateurDTO.RestaurateurRq src);

	RestaurateurDTO.RestaurateurRs toRestaurateurRs(Restaurateur restaurateur);

	RestaurantDTO.RestaurantRs toRestaurantRs(Restaurant restaurant);

	FoodDTO.FoodRs toFoodRs(Food food);
}
