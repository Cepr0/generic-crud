package io.github.cepr0.demo.model.base;

import io.github.cepr0.crud.model.JpaEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class LongIdEntity extends JpaEntity<Long> {

	@Id
	@GeneratedValue
	private Long id;

	@Version private Long version;
}
