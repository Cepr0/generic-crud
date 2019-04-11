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
import io.github.cepr0.crud.event.CreateEntityEvent;
import io.github.cepr0.crud.event.DeleteEntityEvent;
import io.github.cepr0.crud.event.UpdateEntityEvent;
import io.github.cepr0.crud.mapper.CrudMapper;
import io.github.cepr0.crud.model.IdentifiableEntity;
import io.github.cepr0.crud.repo.CrudRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.cepr0.crud.support.CrudUtils.copyNonNullProperties;

/**
 * Base implementation of {@link CrudService}.
 *
 * @param <T>  type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 * @param <Q>  type of the input (request) DTO
 * @param <S>  type of the output (response) DTO
 * @author Sergei Poznanski
 */
@Transactional
public abstract class AbstractCrudService<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>>
		implements CrudService<T, ID, Q, S> {

	protected final CrudRepo<T, ID> repo;
	protected final CrudMapper<T, Q, S> mapper;

	@Autowired protected ApplicationEventPublisher publisher;

	protected AbstractCrudService(final CrudRepo<T, ID> repo, final CrudMapper<T, Q, S> mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 * <br/>
	 * Publishes a new {@link CreateEntityEvent} which contains the saved entity
	 * for post-processing in the custom event listener, transactional or not.
	 */
	@NonNull
	@Override
	public T create(@NonNull final T source) {
		T entity = repo.create(source);
		publisher.publishEvent(new CreateEntityEvent<>(entity));
		return entity;
	}

	/**
	 * {@inheritDoc}
	 * <br/>
	 * Publishes a new {@link CreateEntityEvent} which contains the saved entity
	 * for post-processing in the custom event listener, transactional or not.
	 */
	@NonNull
	@Override
	public S create(@NonNull final Q source) {
		T entity = mapper.toCreate(source);
		onCreate(source, entity);
		repo.create(entity);
		publisher.publishEvent(new CreateEntityEvent<>(entity));
		return mapper.toResponse(entity);
	}

	/**
	 * {@inheritDoc}
	 * <br/>
	 * Publishes a new {@link UpdateEntityEvent} which contains the updated entity
	 * for post-processing in the custom event listener, transactional or not.
	 */
	@NonNull
	@Override
	public Optional<T> update(final ID id, final T source) {
		return repo.update(id, source, (s, t) -> copyNonNullProperties(s, t, ignoredProps()))
				.map(entity -> {
					publisher.publishEvent(new UpdateEntityEvent<>(entity));
					return entity;
				});
	}

	/**
	 * {@inheritDoc}
	 * <br/>
	 * Publishes a new {@link UpdateEntityEvent} which contains the updated entity
	 * for post-processing in the custom event listener, transactional or not.
	 */
	@NonNull
	@Override
	public Optional<S> update(final ID id, final Q source) {
		CallbackMapperAdapter<Q, T> adapter = new CallbackMapperAdapter<>(mapper::toUpdate, this::onUpdate);
		return repo.update(id, source, adapter)
				.map(entity -> {
					publisher.publishEvent(new UpdateEntityEvent<>(entity));
					return mapper.toResponse(entity);
				});
	}

	/**
	 * {@inheritDoc}
	 * <br/>
	 * Publishes a new {@link DeleteEntityEvent} which contains the deleted entity
	 * for post-processing in the custom event listener, transactional or not.
	 */
	@Override
	public boolean delete(@NonNull final ID id) {
		return repo.delete(id).map(deleted -> {
			publisher.publishEvent(new DeleteEntityEvent<>(deleted));
			return true;
		}).orElse(false);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public Optional<T> getOneT(@NonNull final ID id) {
		return repo.getById(id);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public Optional<S> getOne(@NonNull final ID id) {
		return getOneT(id).map(mapper::toResponse);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<T> getAllT() {
		return repo.getAll();
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<S> getAll() {
		return repo.getAll().stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public Page<T> getAllT(final Pageable pageable) {
		return repo.getAll(pageable);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public Page<S> getAll(final Pageable pageable) {
		return repo.getAll(pageable).map(mapper::toResponse);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<T> getAllT(final Sort sort) {
		return repo.getAll(sort);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<S> getAll(final Sort sort) {
		return repo.getAll(sort).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	/**
	 * Provides an array of bean properties to be ignored
	 * when the entity is updated in {@link AbstractCrudService#update(Serializable, IdentifiableEntity)}.<br/>
	 * You can override this method in your implementation of this service and provide your own list of ignored properties.
	 *
	 * @return an array of ignored properties.
	 */
	protected String[] ignoredProps() {
		return new String[]{"id", "version", "createdAt", "updatedAt"};
	}

	/**
	 * Callback method that is called before creating the entity. Can be overridden to implement custom pre-processing.
	 *
	 * @param request input DTO related to the entity
	 * @param entity that is creating, will never be {@code null}
	 */
	@NonNull
	protected void onCreate(@NonNull final Q request, @NonNull final T entity) {
	}

	/**
	 * Callback method that is called before updating the entity. Can be overridden to implement custom pre-processing.
	 *
	 * @param request input DTO related to the entity
	 * @param entity that is updating, will never be {@code null}
	 */
	@NonNull
	protected void onUpdate(@NonNull final Q request, @NonNull final T entity) {
	}
}
