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

package io.github.cepr0.crud.repo;

import io.github.cepr0.crud.model.IdentifiableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Repository interface of generic CRUD operations for a specific entity type and its identifier.
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 *
 * @author Sergei Poznanski
 */
public interface CrudRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> {
	/**
	 * Creates (saves) a given entity.
	 *
	 * @param entity must not be {@code null}
	 * @return created (saved) entity, will never be {@code null}
	 */
	@NonNull T create(@NonNull T entity);

	/**
	 * Updates an entity, found by its id, with a given source and a mapper.
	 *
	 * @param id must not be {@code null}
	 * @param source must not be {@code null}
	 * @param mapper that maps the 'source' to updated entity, must not be {@code null}
	 * @param <S> type of the source which properties are used to update the found entity
	 * @return updated entity, will never be {@code null}
	 */
	@NonNull <S> Optional<T> update(@NonNull ID id, @NonNull S source, @NonNull BiFunction<S, T, T> mapper);

	/**
	 * Deletes an entity by its id, then returns deleted entity.
	 *
	 * @param id must not be {@code null}
	 * @return the deleted entity with the given id or {@code Optional#empty()} if none found
	 */
	@NonNull Optional<T> delete(@NonNull ID id);

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return the entity with the given id or {@code Optional#empty()} if none found
	 */
	@NonNull Optional<T> getById(@NonNull ID id);

	/**
	 * Returns all instances of the entity.
	 *
	 * @return all entities
	 */
	@NonNull List<T> getAll();

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable must not be {@code null}
	 * @return a page of entities
	 */
	@NonNull Page<T> getAll(@NonNull Pageable pageable);

	/**
	 * Returns all entities sorted by the given options.
	 *
	 * @param sort must not be {@code null}
	 * @return all entities sorted by the given options
	 */
	@NonNull List<T> getAll(@NonNull Sort sort);
}
