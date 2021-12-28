package org.devocative.onfood.repository;

import org.devocative.onfood.model.Food;
import org.springframework.stereotype.Repository;

@Repository
public interface IFoodRepository extends IBaseRepository<Food, Long> {
}
