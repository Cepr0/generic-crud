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

package io.github.cepr0.crud.service;

import io.github.cepr0.crud.dto.CrudRequest;
import io.github.cepr0.crud.dto.CrudResponse;
import io.github.cepr0.crud.model.IdentifiableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base service interface provides CRUD operations for entities and their related DTOs.
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 * @param <Q> type of the input (request) DTO
 * @param <S> type of the output (response) DTO
 *
 * @author Sergei Poznanski
 */
public interface CrudService<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {

	/**
	 * Creates (saves) an entity from its input (request) DTO.
	 *
	 * @param source input (request) DTO, must not be {@code null}
	 * @return output (response) DTO of the created entity, will never be {@code null}
	 */
	@NonNull S create(@NonNull Q source);

	/**
	 * Creates (saves) an entity.
	 *
	 * @param source an entity which extends {@link IdentifiableEntity}, must not be {@code null}
	 * @return created entity, will never be {@code null}
	 */
	@NonNull T create(@NonNull T source);

	/**
	 * Updates an entity found by its id, with a given source as an input (request) DTO.
	 *
	 * @param id, must not be {@code null}
	 * @param source, must not be {@code null}
	 * @return output (response) DTO of the updated entity, will never be {@code null}
	 */
	@NonNull Optional<S> update(@NonNull ID id, @NonNull Q source);

	/**
	 * Updates an entity, found by its id, with a given source entity.
	 *
	 * @param id must not be {@code null}
	 * @param source must not be {@code null}
	 * @return updated entity, will never be {@code null}
	 */
	@NonNull Optional<T> update(@NonNull ID id, @NonNull T source);

	/**
	 * Deletes an entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return {@code true} in case of success, {@code false} - otherwise
	 */
	@NonNull boolean delete(@NonNull ID id);

	/**
	 * Returns output (response) DTO of the related entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return output (response) DTO related to the found entity or {@code Optional#empty()} if none found
	 */
	@NonNull Optional<S> getOne(@NonNull ID id);

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return the found entity or {@code Optional#empty()} if none found
	 */
	@NonNull Optional<T> getOneT(@NonNull ID id);

	/**
	 * Retrieves output (response) DTOs of all related entities.
	 *
	 * @return a list of output (response) DTOs of all related entities, will never be {@code null}
	 */
	@NonNull List<S> getAll();

	/**
	 * Retrieves all entities.
	 *
	 * @return a list of all entities, will never be {@code null}
	 */
	@NonNull List<T> getAllT();

	/**
	 * Retrieves a {@link Page} of output (response) DTOs meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable must not be {@code null}
	 * @return a page of DTO
	 */
	@NonNull Page<S> getAll(@NonNull Pageable pageable);

	/**
	 * Retrieves a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable must not be {@code null}
	 * @return a page of entities
	 */
	@NonNull Page<T> getAllT(@NonNull Pageable pageable);

	/**
	 * Retrieves output (response) DTOs of all related entities sorted by the given sort parameter.
	 *
	 * @param sort must not be {@code null}
	 * @return a sorted list of output (response) DTOs of all related entities
	 */
	@NonNull List<S> getAll(@NonNull Sort sort);

	/**
	 * Retrieves all entities sorted by the given sort parameter.
	 *
	 * @param sort must not be {@code null}
	 * @return a sorted list of all entities
	 */
	@NonNull List<T> getAllT(@NonNull Sort sort);
}
