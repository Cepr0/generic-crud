package io.github.cepr0.crud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@Accessors(chain = true)
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class IntIdEntity extends JpaEntity<Integer> {
	@Id
	@GeneratedValue
	private Integer id;

	@Version
	private Integer version;
}
