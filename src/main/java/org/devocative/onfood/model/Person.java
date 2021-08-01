package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(of = {"cell", "firstName", "lastName"})
@MappedSuperclass
public abstract class Person extends Auditable {
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
