package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static org.devocative.onfood.model.Restaurateur.UC_MAIN;


@Getter
@Setter
@Entity
@Table(name = "t_restaurateur", uniqueConstraints = {
	@UniqueConstraint(name = UC_MAIN, columnNames = "c_cell")
})
public class Restaurateur extends Person {
	public static final String UC_MAIN = "uc_restaurateur_main";
}
