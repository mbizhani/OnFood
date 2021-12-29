package org.devocative.onfood;

import org.devocative.onfood.model.EStatus;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurant;
import org.devocative.onfood.repository.IFoodRepository;
import org.devocative.onfood.repository.IRestaurantRepository;
import org.devocative.onfood.search.SearchSpecification;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ERangeValue.Between;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ESingleValue.*;
import static org.devocative.onfood.search.expression.logical.LogicalOperator.EMultiOperand.And;
import static org.devocative.onfood.search.expression.logical.LogicalOperator.EMultiOperand.Or;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class TestSearchSpecification {

	@Autowired
	private IRestaurantRepository restaurantRepository;

	@Autowired
	private IFoodRepository foodRepository;

	@BeforeEach
	public void init() {
		final Restaurant blackJoe = restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Black Joe")
			.setRate(2.5f)
			.setStatus(EStatus.Open)
			.setOpeningDate(LocalDate.of(2019, Month.JANUARY, 1)));
		final Restaurant blackJungle = restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Black Jungle")
			.setRate(3f)
			.setStatus(EStatus.Open)
			.setOpeningDate(LocalDate.of(2019, Month.FEBRUARY, 1)));
		final Restaurant kenzo = restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Kenzo")
			.setRate(2f)
			.setStatus(EStatus.Closed)
			.setOpeningDate(LocalDate.of(2017, Month.APRIL, 11)));

		foodRepository.saveAllAndFlush(Arrays.asList(
			new Food().setName("Pizza 1").setPrice(100).setRestaurant(blackJoe),
			new Food().setName("Burger").setPrice(50).setRestaurant(blackJoe),
			new Food().setName("Roast Beef").setPrice(60).setRestaurant(blackJoe),
			new Food().setName("Chicken Burger").setPrice(50).setRestaurant(blackJoe),
			new Food().setName("Chicken Salad").setPrice(30).setRestaurant(blackJoe),

			new Food().setName("Chicken Pasta").setPrice(120).setRestaurant(blackJungle),
			new Food().setName("Alfredo").setPrice(75).setRestaurant(blackJungle),
			new Food().setName("Beef Alfredo").setPrice(85).setRestaurant(blackJungle),
			new Food().setName("Green Salad").setPrice(40).setRestaurant(blackJungle)
		));
	}

	@Test
	public void testPrice() {
		{
			SingleValueComparisonExpression expression = new SingleValueComparisonExpression(GreaterThan, "price", "85");
			final List<Food> list = foodRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(food -> System.out.println("** TEST: " + food));
			assertEquals(2, list.size());

			final List<String> names = list.stream().map(Food::getName).collect(Collectors.toList());
			assertTrue(names.contains("Pizza 1"));
			assertTrue(names.contains("Chicken Pasta"));
		}

		{
			MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(And,
				new SingleValueComparisonExpression(GreaterThanEqual, "rate", "3"),
				new RangeValueComparisonExpression(Between, "openingDate",
					LocalDate.of(2019, Month.JANUARY, 1).toString(),
					LocalDate.of(2020, Month.JANUARY, 1).toString()));

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> System.out.println("** TEST: " + restaurant));

			assertEquals(1, list.size());
			assertEquals("Black Jungle", list.get(0).getName());
		}

		{
			MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(Or,
				new SingleValueComparisonExpression(LessThan, "rate", "3"),
				new SingleValueComparisonExpression(Equal, "status", "Closed")
			);

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> System.out.println("** TEST: " + restaurant));
			assertEquals(2, list.size());

			final List<String> names = list.stream().map(Restaurant::getName).collect(Collectors.toList());
			assertTrue(names.contains("Kenzo"));
			assertTrue(names.contains("Black Joe"));
		}

		{
			MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(And,
				new SingleValueComparisonExpression(Contain, "name", "Chicken"),
				new SingleValueComparisonExpression(GreaterThan, "restaurant.rate", "2"));

			final List<Food> list = foodRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(food -> System.out.println("** TEST: " + food));
		}
	}
}
