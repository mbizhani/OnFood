package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@ToString(of = {"cell", "firstName", "lastName"})
@MappedSuperclass
public abstract class Person extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "c_first_name", nullable = false)
	private String firstName;

	@NotNull
	@Column(name = "c_last_name", nullable = false)
	private String lastName;

	@Column(name = "c_cell", nullable = false)
	private String cell;

	@Column(name = "c_password")
	private String password;

	@Email
	@Column(name = "c_email")
	private String email;

	@Column(name = "d_birth_date")
	private LocalDate birthDate;
}
