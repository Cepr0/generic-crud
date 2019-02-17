package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.IdentifiableEntity;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class DeleteEntityEvent<T extends IdentifiableEntity, ID extends Serializable> {

	private final ID entityId;
	private final Class<T> entityClass;

	public DeleteEntityEvent(ID entityId, Class<T> entityClass) {
		this.entityId = entityId;
		this.entityClass = entityClass;
	}
}
