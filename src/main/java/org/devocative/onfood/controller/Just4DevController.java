package org.devocative.onfood.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.dto.RestaurateurDTO;
import org.devocative.onfood.iservice.IRestaurateurService;
import org.devocative.onfood.model.EStatus;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurant;
import org.devocative.onfood.repository.IFoodRepository;
import org.devocative.onfood.repository.IRestaurantRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Profile("!production")
@RestController
@RequestMapping("/api/j4d")
public class Just4DevController {
	private final IRestaurateurService restaurateurService;
	private final IRestaurantRepository restaurantRepository;
	private final IFoodRepository foodRepository;

	@PostConstruct
	public void init() {
		final long count = restaurantRepository.count();
		log.info("Just4DevController - Count = {}", count);
		if (count == 0) {
			final Restaurant blackJoe = restaurantRepository.saveAndFlush(new Restaurant()
				.setName("Black Joe")
				.setRate(2.5f)
				.setStatus(EStatus.Open)
				.setOpeningDate(LocalDate.of(2019, Month.JANUARY, 1))
				.setStartTime(LocalTime.of(10, 0))
				.setCloseTime(LocalTime.of(0, 0)));
			final Restaurant blackJungle = restaurantRepository.saveAndFlush(new Restaurant()
				.setName("Black Jungle")
				.setRate(3f)
				.setStatus(EStatus.Open)
				.setOpeningDate(LocalDate.of(2019, Month.FEBRUARY, 1))
				.setStartTime(LocalTime.of(11, 30)));
			restaurantRepository.saveAndFlush(new Restaurant()
				.setName("Kenzo")
				.setRate(2f)
				.setStatus(EStatus.Closed)
				.setOpeningDate(LocalDate.of(2017, Month.APRIL, 11)));
			final Restaurant milano = restaurantRepository.saveAndFlush(new Restaurant()
				.setName("Milano")
				.setStatus(EStatus.Opening));

			foodRepository.saveAllAndFlush(Arrays.asList(
				new Food().setName("Pizza 1").setPrice(100).setRestaurant(blackJoe),
				new Food().setName("Burger").setPrice(50).setRestaurant(blackJoe),
				new Food().setName("Roast Beef").setPrice(60).setRestaurant(blackJoe),
				new Food().setName("Chicken Burger").setPrice(50).setRestaurant(blackJoe),
				new Food().setName("Chicken Salad").setPrice(30).setRestaurant(blackJoe),

				new Food().setName("Chicken Pasta").setPrice(120).setRestaurant(blackJungle),
				new Food().setName("Alfredo").setPrice(75).setRestaurant(blackJungle),
				new Food().setName("Beef Alfredo").setPrice(85).setRestaurant(blackJungle),
				new Food().setName("Green Salad").setPrice(40).setRestaurant(blackJungle),

				new Food().setName("Chicken").setPrice(70).setRestaurant(milano)
			));
		}
	}

	@GetMapping("/registrations/{cell}")
	public ResponseEntity<RestaurateurDTO.RegistrationCodeRs> getRegistrationCode(@PathVariable String cell) {
		log.warn("Get Registrations Code: cell=[{}]", cell);
		return ResponseEntity.ok(new RestaurateurDTO.RegistrationCodeRs(restaurateurService.getRegistrationCode(cell)));
	}
}
