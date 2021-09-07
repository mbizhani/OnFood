package org.devocative.onfood.repository;

import org.devocative.onfood.model.Restaurateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRestaurateurRepository extends JpaRepository<Restaurateur, Long> {
	Optional<Restaurateur> findByCell(String cell);
}
