package io.github.cepr0.demo.model;

import io.github.cepr0.demo.model.base.UuidEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
@DynamicInsert
@DynamicUpdate
public class Car extends UuidEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Person person;

	@Column(nullable = false, length = 32)
	private String brand;

	@Column(nullable = false, length = 32)
	private String model;

	@Column(nullable = false)
	private Integer year;
}
