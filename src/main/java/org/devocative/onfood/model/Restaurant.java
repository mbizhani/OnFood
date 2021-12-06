package org.devocative.onfood.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.devocative.onfood.model.Restaurant.UC_MAIN;

@Getter
@Setter
@Entity
@Table(name = "t_restaurant", uniqueConstraints = {
	@UniqueConstraint(name = UC_MAIN, columnNames = "c_name")
})
public class Restaurant {
	public static final String UC_MAIN = "uc_restaurant_main";

	// ------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "c_name", nullable = false)
	private String name;

	@Column(name = "n_rate")
	private Float rate;

	@Column(name = "e_status")
	private EStatus status;

	@Column(name = "d_opening_date")
	private LocalDate openingDate;

	@Column(name = "d_start_time")
	private LocalTime startTime;

	@Column(name = "d_close_time")
	private LocalTime closeTime;
}
