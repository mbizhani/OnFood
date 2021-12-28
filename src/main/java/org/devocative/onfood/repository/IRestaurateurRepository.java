package org.devocative.onfood.repository;

import org.devocative.onfood.model.Restaurateur;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRestaurateurRepository extends IBaseRepository<Restaurateur, Long> {
	Optional<Restaurateur> findByCell(String cell);
}
