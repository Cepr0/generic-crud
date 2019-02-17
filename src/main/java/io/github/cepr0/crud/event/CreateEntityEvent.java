package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.IdentifiableEntity;
import lombok.Getter;

@Getter
public class CreateEntityEvent<T extends IdentifiableEntity> {

	private final T entity;

	public CreateEntityEvent(final T entity) {
		this.entity = entity;
	}
}
