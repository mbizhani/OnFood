package org.devocative.onfood.service;

import lombok.RequiredArgsConstructor;
import org.devocative.onfood.dto.FoodDTO;
import org.devocative.onfood.dto.SearchDTO;
import org.devocative.onfood.iservice.IBeanMapper;
import org.devocative.onfood.iservice.IFoodService;
import org.devocative.onfood.repository.IFoodRepository;
import org.devocative.onfood.search.SearchUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FoodService implements IFoodService {
	private final IFoodRepository foodRepository;
	private final IBeanMapper beanMapper;

	@Transactional
	@Override
	public SearchDTO.SearchRs<FoodDTO.FoodRs> search(SearchDTO.SearchRq searchRq) {
		return SearchUtil.search(foodRepository, searchRq, beanMapper::toFoodRs);
	}
}
