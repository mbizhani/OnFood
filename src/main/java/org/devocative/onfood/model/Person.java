package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static org.devocative.onfood.model.Person.UC_MAIN;


@Getter
@Setter
@Entity
@Table(name = "t_person", uniqueConstraints = {
	@UniqueConstraint(name = UC_MAIN, columnNames = "c_cell")
})
public class Person {
	public static final String UC_MAIN = "uc_person_main";

	// ------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "c_first_name")
	private String firstName;

	@Column(name = "c_last_name")
	private String lastName;

	@Column(name = "c_cell", nullable = false)
	private String cell;

	@Column(name = "c_password")
	private String password;

	@Column(name = "c_email")
	private String email;
}
