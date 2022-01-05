package org.devocative.onfood.dto;

import lombok.Getter;
import lombok.Setter;
import org.devocative.onfood.model.EStatus;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public abstract class RestaurantDTO {
	@NotEmpty
	private String name;
	private Float rate;
	private EStatus status;
	private LocalDate openingDate;
	private LocalTime startTime;
	private LocalTime closeTime;

	@Getter
	@Setter
	public static class RestaurantRs extends RestaurantDTO {
		private Long id;
	}
}
