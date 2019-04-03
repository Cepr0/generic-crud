package io.github.cepr0.demo.model;

import io.github.cepr0.demo.model.base.UuidEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "people")
@DynamicInsert
@DynamicUpdate
public class Person extends UuidEntity {

	@Column(nullable = false, length = 32)
	private String name;

	@BatchSize(size = 20)
	@OneToMany(mappedBy = "person")
	private Set<Car> cars;
}
