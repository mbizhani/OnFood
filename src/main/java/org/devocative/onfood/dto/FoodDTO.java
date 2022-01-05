package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public abstract class FoodDTO {
	@NotEmpty
	private String name;

	@Min(1)
	@NotNull
	private Integer price;

	@Getter
	@Setter
	public static class FoodRs extends FoodDTO {
		private Long id;

		private RestaurantDTO.RestaurantRs restaurant;
	}

}
