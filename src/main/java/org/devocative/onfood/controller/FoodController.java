package org.devocative.onfood.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.dto.FoodDTO;
import org.devocative.onfood.dto.SearchDTO;
import org.devocative.onfood.iservice.IFoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/foods")
public class FoodController {
	private final IFoodService foodService;

	@PostMapping("/searches")
	public ResponseEntity<SearchDTO.SearchRs<FoodDTO.FoodRs>> search(@Valid @RequestBody SearchDTO.SearchRq searchRq) {
		return ResponseEntity.ok(foodService.search(searchRq));
	}
}
