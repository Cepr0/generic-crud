package io.github.cepr0.crud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "models")
public class Model extends IntIdEntity {
	@Column(length = 32, nullable = false)
	private String text;

	@Column(nullable = false)
	private Integer number;
}
