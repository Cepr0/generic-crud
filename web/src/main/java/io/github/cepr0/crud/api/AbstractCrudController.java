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

package io.github.cepr0.crud.api;

import io.github.cepr0.crud.dto.CrudRequest;
import io.github.cepr0.crud.dto.CrudResponse;
import io.github.cepr0.crud.model.IdentifiableEntity;
import io.github.cepr0.crud.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Base controller which provides CRUD operations for {@link IdentifiableEntity} entities,
 * {@link CrudRequest} requests and {@link CrudResponse} responses.
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 * @param <Q> type of the input (request) DTO
 * @param <S> type of the output (response) DTO
 *
 * @author Sergei Poznanski
 */
public abstract class AbstractCrudController<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {

	protected final CrudService<T, ID, Q, S> service;

	public AbstractCrudController(@NonNull final CrudService<T, ID, Q, S> service) {
		this.service = service;
	}

	/**
	 * Creates an entity based on its input (request) DTO.
	 *
	 * @param request must not be {@code null}
	 * @return {@link ResponseEntity} with output (response) DTO as a body of the created entity,
	 * and 201 (Created) HTTP status
	 */
	@NonNull
	public ResponseEntity<S> create(@NonNull final Q request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
	}

	/**
	 * Updates an entity by its id, with provided input (request) DTO.
	 *
	 * @param id must not be {@code null}
	 * @param request must not be {@code null}
	 * @return {@link ResponseEntity} with output (response) DTO of the updated entity as a body,
	 * 200 (Ok) HTTP status if the entity was found, and 404 (Not Found) - otherwise.
	 */
	@NonNull
	public ResponseEntity<S> update(@NonNull final ID id, @NonNull final Q request) {
		return service.update(id, request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Deletes an entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return {@link ResponseEntity} with empty body,
	 * and 204 (No Content) HTTP status if the entity was found, and 404 (Not Found) - otherwise.
	 */
	@NonNull
	public ResponseEntity delete(@NonNull final ID id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Retrieves the entity by its id.
	 *
	 * @param id must not be {@code null}
	 * @return {@link ResponseEntity} with output (response) DTO of the found entity as a body,
	 * 200 (Ok) HTTP status if the entity was found, and 404 (Not Found) - otherwise.
	 */
	@NonNull
	public ResponseEntity<S> getOne(@NonNull final ID id) {
		return service.getOne(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Retrieves a {@link Page} of output (response) DTOs meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable pageable must not be {@code null}
	 * @return {@link ResponseEntity} with a page of output (response) DTOs as a body, and with 200 (Ok) HTTP status
	 */
	@NonNull
	public ResponseEntity<Page<S>> getAll(@NonNull final Pageable pageable) {
		return ResponseEntity.ok(service.getAll(pageable));
	}

	/**
	 * Retrieves all entities sorted by the given sort parameter.
	 *
	 * @param sort must not be {@code null}
	 * @return {@link ResponseEntity} with a list of output (response) DTOs as a body, and with 200 (Ok) HTTP status
	 */
	@NonNull
	public ResponseEntity<List<S>> getAll(@NonNull final Sort sort) {
		return ResponseEntity.ok(service.getAll(sort));
	}

	/**
	 * Retrieves all entities.
	 *
	 * @return {@link ResponseEntity} with a list of output (response) DTOs as a body, and with 200 (Ok) HTTP status
	 */
	@NonNull
	public ResponseEntity<List<S>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
}
