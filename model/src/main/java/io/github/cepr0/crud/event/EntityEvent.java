/*
 * Copyright 2019 Generic-CRUD contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.IdentifiableEntity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Base class for all entity events.
 *
 * @author Sergei Poznanski
 */
public class EntityEvent<T extends IdentifiableEntity> implements Serializable {

	/**
	 * Entity related to the event.
	 */
	private final T entity;

	public EntityEvent(final T entity) {
		this.entity = Objects.requireNonNull(entity, "Parameter 'entity' must not be null!");
	}

	/**
	 * Can be used in 'conditional' parameter of event listeners to filter events.
	 *
	 * @param simpleClassName a simple class name to compare with the entity
	 * @return return true if the parameter is equal to the simple class name of the entity
	 */
	public boolean contain(String simpleClassName) {
		return Objects.requireNonNull(entity, "Parameter 'simpleClassName' must not be null!")
				.getClass()
				.getSimpleName()
				.equals(simpleClassName);
	}

	public T getEntity() {
		return entity;
	}
}
