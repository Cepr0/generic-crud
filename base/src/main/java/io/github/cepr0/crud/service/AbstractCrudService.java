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
import io.github.cepr0.crud.event.EntityEvent;
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
	 * <p>
	 * Publishes 'entity is created' {@link EntityEvent} if {@link AbstractCrudService#onCreateEvent} method returns a new one.
	 * <p>
	 * The event contains the saved entity, and can be post-processed in the custom event listener.
	 */
	@NonNull
	@Override
	public T create(@NonNull final T source) {
		T entity = repo.create(source);

		EntityEvent<T> event = onCreateEvent(entity);
		if (event != null) publisher.publishEvent(event);

		return entity;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Publishes 'entity is created' {@link EntityEvent} if {@link AbstractCrudService#onCreateEvent} method returns a new one.
	 * <p>
	 * The event contains the saved entity, and can be post-processed in the custom event listener.
	 */
	@NonNull
	@Override
	public S create(@NonNull final Q source) {
		T entity = mapper.toCreate(source);
		onCreate(source, entity);
		repo.create(entity);

		EntityEvent<T> event = onCreateEvent(entity);
		if (event != null) publisher.publishEvent(event);

		return mapper.toResponse(entity);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Publishes 'entity is updated' {@link EntityEvent} if {@link AbstractCrudService#onUpdateEvent} method returns a new one.
	 * <p>
	 * The event contains the updated entity, and can be post-processed in the custom event listener.
	 */
	@NonNull
	@Override
	public Optional<T> update(final ID id, final T source) {
		return repo.update(id, source, (s, t) -> copyNonNullProperties(s, t, ignoredProps()))
				.map(entity -> {
					EntityEvent<T> event = onUpdateEvent(entity);
					if (event != null) publisher.publishEvent(event);
					return entity;
				});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Publishes 'entity is updated' {@link EntityEvent} if {@link AbstractCrudService#onUpdateEvent} method returns a new one.
	 * <p>
	 * The event contains the updated entity, and can be post-processed in the custom event listener.
	 */
	@NonNull
	@Override
	public Optional<S> update(final ID id, final Q source) {
		return repo.update(id, source, new CallbackMapperAdapter<>(mapper::toUpdate, this::onUpdate))
				.map(entity -> {
					EntityEvent<T> event = onUpdateEvent(entity);
					if (event != null) publisher.publishEvent(event);
					return mapper.toResponse(entity);
				});
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Publishes 'entity is deleted' {@link EntityEvent} if {@link AbstractCrudService#onDeleteEvent} method returns a new one.
	 * <p>
	 * The event contains the deleted entity, and can be post-processed in the custom event listener.
	 */
	@Override
	public boolean delete(@NonNull final ID id) {
		return repo.delete(id).map(deleted -> {
			EntityEvent<T> event = onDeleteEvent(deleted);
			if (event != null) publisher.publishEvent(event);
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
	protected void onCreate(@NonNull Q request, @NonNull T entity) {
	}

	/**
	 * Callback method that is called before updating the entity. Can be overridden to implement custom pre-processing.
	 *
	 * @param request input DTO related to the entity
	 * @param entity that is updated, will never be {@code null}
	 */
	@NonNull
	protected void onUpdate(@NonNull Q request, @NonNull T entity) {
	}

	/**
	 * Factory callback method is called after entity is created,
	 * to create 'entity is created' {@link EntityEvent} and, if it isn't {@code null}, to publish it.
	 *
	 * @param entity created entity
	 * @return an event or {@code null} if none
	 */
	protected EntityEvent<T> onCreateEvent(@NonNull T entity) {
		return null;
	}

	/**
	 * Factory callback method is called after entity is updated,
	 * to create 'entity is updated' {@link EntityEvent} and, if it isn't {@code null}, to publish it.
	 *
	 * @param entity updated entity
	 * @return an event or {@code null} if none
	 */
	protected EntityEvent<T> onUpdateEvent(@NonNull T entity) {
		return null;
	}

	/**
	 * Factory callback method is called after entity is deleted,
	 * to create 'entity is deleted' {@link EntityEvent} and, if it isn't {@code null}, to publish it.
	 *
	 * @param entity deleted entity
	 * @return an event or {@code null} if none
	 */
	protected EntityEvent<T> onDeleteEvent(@NonNull T entity) {
		return null;
	}
}
