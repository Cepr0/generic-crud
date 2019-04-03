package io.github.cepr0.demo.model;

import io.github.cepr0.demo.model.base.LongIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
public class User extends LongIdEntity {

	@Column(nullable = false, length = 32)
	private String name;
}
