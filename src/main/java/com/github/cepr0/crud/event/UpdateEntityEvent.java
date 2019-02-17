package com.github.cepr0.crud.event;

import com.github.cepr0.crud.model.IdentifiableEntity;
import lombok.Getter;

@Getter
public class UpdateEntityEvent<T extends IdentifiableEntity> {
	
	private final T entity;

	public UpdateEntityEvent(final T entity) {
		this.entity = entity;
	}
}
