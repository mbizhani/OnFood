package org.devocative.onfood;

import lombok.extern.slf4j.Slf4j;
import org.devocative.onfood.dto.SearchDTO;
import org.devocative.onfood.model.EStatus;
import org.devocative.onfood.model.Food;
import org.devocative.onfood.model.Restaurant;
import org.devocative.onfood.repository.IFoodRepository;
import org.devocative.onfood.repository.IRestaurantRepository;
import org.devocative.onfood.search.PageRequest;
import org.devocative.onfood.search.SearchSpecification;
import org.devocative.onfood.search.SearchUtil;
import org.devocative.onfood.search.SortExpression;
import org.devocative.onfood.search.expression.comparison.MultiValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.NoValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.RangeValueComparisonExpression;
import org.devocative.onfood.search.expression.comparison.SingleValueComparisonExpression;
import org.devocative.onfood.search.expression.logical.MultiOperandLogicalExpression;
import org.devocative.onfood.search.expression.logical.SingleOperandLogicalExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.EMultiValue.In;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.EMultiValue.NotIn;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ENoValue.Empty;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ENoValue.NotEmpty;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ERangeValue.Between;
import static org.devocative.onfood.search.expression.comparison.ComparisonOperator.ESingleValue.*;
import static org.devocative.onfood.search.expression.logical.LogicalOperator.EMultiOperand.And;
import static org.devocative.onfood.search.expression.logical.LogicalOperator.EMultiOperand.Or;
import static org.devocative.onfood.search.expression.logical.LogicalOperator.ESingleOperand.Not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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
			.setOpeningDate(LocalDate.of(2019, Month.JANUARY, 1))
			.setStartTime(LocalTime.of(10, 0))
			.setCloseTime(LocalTime.of(0, 0)));
		final Restaurant blackJungle = restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Black Jungle")
			.setRate(3f)
			.setStatus(EStatus.Open)
			.setOpeningDate(LocalDate.of(2019, Month.FEBRUARY, 1)));
		restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Kenzo")
			.setRate(2f)
			.setStatus(EStatus.Closed)
			.setOpeningDate(LocalDate.of(2017, Month.APRIL, 11)));
		final Restaurant milano = restaurantRepository.saveAndFlush(new Restaurant()
			.setName("Milano")
			.setStartTime(LocalTime.of(11, 30)));

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

	@Test
	public void testExpressions() {
		{
			final SingleValueComparisonExpression expression = new SingleValueComparisonExpression(GreaterThan, "price", "85");
			final List<Food> list = foodRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(food -> log.info("** price > ?: {}", food));
			assertEquals(2, list.size());

			final List<String> names = list.stream().map(Food::getName).collect(Collectors.toList());
			assertTrue(names.contains("Pizza 1"));
			assertTrue(names.contains("Chicken Pasta"));
		}

		{
			final MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(And,
				new SingleValueComparisonExpression(GreaterThanEqual, "rate", "3"),
				new RangeValueComparisonExpression(Between, "openingDate",
					LocalDate.of(2019, Month.JANUARY, 1).toString(),
					LocalDate.of(2020, Month.JANUARY, 1).toString()));

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** rate >= ? AND openingDate between ? and ? : {}", restaurant));

			assertEquals(1, list.size());
			assertEquals("Black Jungle", list.get(0).getName());
		}

		{
			final MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(Or,
				new SingleValueComparisonExpression(LessThan, "rate", "3"),
				new SingleValueComparisonExpression(Equal, "status", "Closed")
			);

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** rate < ? OR status = ?: {}", restaurant));
			assertEquals(2, list.size());

			final List<String> names = list.stream().map(Restaurant::getName).collect(Collectors.toList());
			assertTrue(names.contains("Kenzo"));
			assertTrue(names.contains("Black Joe"));
		}

		{
			final MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(And,
				new SingleValueComparisonExpression(Contain, "name", "Chicken"),
				new SingleValueComparisonExpression(GreaterThan, "restaurant.rate", "2"));

			final List<Food> list = foodRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(food -> log.info("** name contains ? AND restaurant.rate > ?: {}", food));
			assertEquals(3, list.size());

			final List<String> names = list.stream().map(Food::getName).collect(Collectors.toList());
			assertTrue(names.contains("Chicken Burger"));
			assertTrue(names.contains("Chicken Salad"));
			assertTrue(names.contains("Chicken Pasta"));
		}

		{
			final MultiOperandLogicalExpression expression = new MultiOperandLogicalExpression(And,
				new NoValueComparisonExpression(NotEmpty, "startTime"),
				new NoValueComparisonExpression(NotEmpty, "closeTime")
			);

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** startTime not empty AND closeTime not empty: {}", restaurant));
			assertEquals(1, list.size());
			assertEquals("Black Joe", list.get(0).getName());
		}

		{
			final NoValueComparisonExpression expression = new NoValueComparisonExpression(Empty, "rate");
			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** rate is empty: {}", restaurant));
			assertEquals(1, list.size());
			assertEquals("Milano", list.get(0).getName());
		}

		{
			final SingleOperandLogicalExpression expression = new SingleOperandLogicalExpression(Not,
				new MultiValueComparisonExpression(In, "status", "Closed"));
			final List<Restaurant> not_in = restaurantRepository.findAll(new SearchSpecification<>(expression));
			not_in.forEach(restaurant -> log.info("** NOT (status in ?): {}", restaurant));

			final List<Restaurant> notIn = restaurantRepository.findAll(new SearchSpecification<>(new MultiValueComparisonExpression(NotIn, "status", "Closed")));
			notIn.forEach(restaurant -> log.info("** status notIn ?: {}", restaurant));

			assertEquals(not_in.size(), notIn.size());
			assertEquals(2, notIn.size());
			assertEquals(not_in.get(0), notIn.get(0));
			assertEquals(not_in.get(1), notIn.get(1));
		}

		{
			final SingleOperandLogicalExpression expression = new SingleOperandLogicalExpression(Not, new MultiOperandLogicalExpression(And,
				new NoValueComparisonExpression(NotEmpty, "startTime"),
				new NoValueComparisonExpression(NotEmpty, "closeTime")
			));

			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** NOT (startTime is not empty AND closeTime is not empty): {}", restaurant));

			assertEquals(3, list.size());
		}

		{
			final RangeValueComparisonExpression expression = new RangeValueComparisonExpression(Between, "startTime", "08:00", "10:00");
			final List<Restaurant> list = restaurantRepository.findAll(new SearchSpecification<>(expression));
			list.forEach(restaurant -> log.info("** startTime between ? and ?: {}", restaurant));

			assertEquals(1, list.size());
			assertEquals("Black Joe", list.get(0).getName());
		}
	}

	@Test
	public void testPagination() {
		final List<String> expectedNames = List.of("Beef Alfredo", "Chicken", "Chicken Pasta", "Pizza 1");

		{
			final Page<Food> page = foodRepository.findAll(
				new SearchSpecification<>(
					new SingleValueComparisonExpression(GreaterThan, "price", "50"),
					List.of(new SortExpression("name"))
				),
				new PageRequest(1, 4)
			);

			final List<Food> list = page.getContent();
			list.forEach(food -> log.info("** paged food: {}", food));

			assertEquals(4, list.size());
			assertEquals(6, page.getTotalElements());

			for (int i = 0; i < expectedNames.size(); i++) {
				assertEquals(expectedNames.get(i), list.get(i).getName());
			}
		}

		{
			final SearchDTO.SearchRq searchRq = new SearchDTO.SearchRq()
				.setStartIndex(1)
				.setCount(4)
				.setFilter(new SingleValueComparisonExpression(GreaterThan, "price", "50"))
				.setSorts(List.of(new SortExpression("name")));

			final SearchDTO.SearchRs<Food> searchRs = SearchUtil.search(foodRepository, searchRq, true, Function.identity());
			assertEquals(4, searchRs.getResult().size());
			assertEquals(6, searchRs.getTotalCount());

			for (int i = 0; i < expectedNames.size(); i++) {
				assertEquals(expectedNames.get(i), searchRs.getResult().get(i).getName());
			}
		}

		{
			final SearchDTO.SearchRq searchRq = new SearchDTO.SearchRq()
				.setStartIndex(1)
				.setCount(4)
				.setFilter(new SingleValueComparisonExpression(GreaterThan, "price", "50"))
				.setSorts(List.of(new SortExpression("name")));

			final SearchDTO.SearchRs<Food> searchRs = SearchUtil.search(foodRepository, searchRq, false, Function.identity());
			assertEquals(4, searchRs.getResult().size());
			assertEquals(5, searchRs.getTotalCount());
		}
	}
}
