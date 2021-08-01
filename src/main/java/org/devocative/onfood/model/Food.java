package org.devocative.onfood.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static org.devocative.onfood.model.Food.UC_MAIN;

@Getter
@Setter
@Entity
@Table(name = "t_food", uniqueConstraints = {
	@UniqueConstraint(name = UC_MAIN, columnNames = {"f_restaurant", "c_name"})
})
public class Food extends Auditable {
	public static final String UC_MAIN = "uc_food_main";

	// ------------------------------

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "c_name", nullable = false)
	private String name;

	@Column(name = "n_price")
	private Integer price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_restaurant", nullable = false, foreignKey = @ForeignKey(name = "fk_food2restaurant"))
	private Restaurant restaurant;

	@Setter(AccessLevel.NONE)
	@Column(name = "f_restaurant", insertable = false, updatable = false, nullable = false)
	private Long restaurantId;
}
