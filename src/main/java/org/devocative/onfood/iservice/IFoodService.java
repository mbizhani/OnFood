package org.devocative.onfood.iservice;

import org.devocative.onfood.dto.FoodDTO;
import org.devocative.onfood.dto.SearchDTO;

public interface IFoodService {
	SearchDTO.SearchRs<FoodDTO.FoodRs> search(SearchDTO.SearchRq searchRq);
}
