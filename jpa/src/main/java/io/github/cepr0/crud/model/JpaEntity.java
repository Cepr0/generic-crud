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

package io.github.cepr0.crud.model;

import java.io.Serializable;

/**
 * Abstract class for the entities which should work in JPA environment.
 * It base only on identifier of the entity, and its {@code equals} and {@code hashCode} methods behave consistently
 * across all entity state transitions (according to
 * <a href="https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/">
 *    Vlad Mihalcea
 * </a>).
 * <br/>
 * This class inherit {@link IdentifiableEntity}, so you can use it to extend your entity from it.
 *
 * @param <ID> type of the entity identifier
 *
 * @author Sergei Poznanski
 */
public abstract class JpaEntity<ID extends Serializable> implements IdentifiableEntity<ID> {

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{id=" + getId() + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return getId() != null && getId().equals(((JpaEntity) o).getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
