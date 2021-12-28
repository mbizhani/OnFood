package org.devocative.onfood.repository;

import org.devocative.onfood.model.Restaurant;
import org.springframework.stereotype.Repository;

@Repository
public interface IRestaurantRepository extends IBaseRepository<Restaurant, Long> {
}
