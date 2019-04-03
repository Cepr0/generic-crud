package io.github.cepr0.demo.model.base;

import io.github.cepr0.crud.model.JpaEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class UuidEntity extends JpaEntity<UUID> {

	@Id private UUID id;
	@Version private Long version;

	public UuidEntity() {
		this.id = UUID.randomUUID();
	}

	@Override
	public int hashCode() {
		Assert.notNull(id, "id must not be null!");
		return id.hashCode();
	}
}
