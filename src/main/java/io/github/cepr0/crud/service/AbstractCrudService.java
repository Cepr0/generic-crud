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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@Transactional
public abstract class AbstractCrudService<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>>
		implements CrudService<T, ID, Q, S> {

	protected final CrudRepo<T, ID> repo;
	protected final CrudMapper<T, ID, Q, S> mapper;

	protected ApplicationEventPublisher publisher;

	public AbstractCrudService(@NonNull final CrudRepo<T, ID> repo, @NonNull final CrudMapper<T, ID, Q, S> mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	@Autowired
	public void setPublisher(@NonNull final ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@NonNull
	@Override
	public S create(@NonNull final Q request) {
		T entity = onCreate(mapper.toCreate(request), request);
		return mapper.toResponse(create(entity));
	}

	@NonNull
	public T create(@NonNull final T entity) {
		repo.save(entity);
		publisher.publishEvent(new CreateEntityEvent<>(entity));
		return entity;
	}

	@NonNull
	@Override
	public Optional<S> update(@NonNull final ID id, @NonNull final Q request) {
		return repo.getToUpdateById(id)
				.map(entity -> {
					T updated = mapper.toUpdate(onUpdate(entity, request), request);
					return mapper.toResponse(update(updated));
				});
	}

	@NonNull
	public T update(@NonNull final T entity) {
		repo.flush();
		publisher.publishEvent(new UpdateEntityEvent<>(entity));
		return entity;
	}

	@NonNull
	@Override
	public boolean delete(@NonNull final ID id) {
		onDelete(id);
		T reference = repo.getOne(id);
		if (repo.delById(id) > 0) {
			publisher.publishEvent(new DeleteEntityEvent<>(id, reference.getClass()));
			return true;
		} else {
			return false;
		}
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public Optional<S> getOne(@NonNull final ID id) {
		return repo.getById(id).map(mapper::toResponse);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<S> getAll(@NonNull final Pageable pageable) {
		return repo.getAll(pageable).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<S> getAll(@NonNull final Sort sort) {
		return repo.getAll(sort).stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	public List<S> getAll() {
		return repo.getAll().stream().map(mapper::toResponse).collect(Collectors.toList());
	}

	@NonNull
	protected T onCreate(@NonNull T entity, @NonNull Q request) {
		return entity;
	}

	@NonNull
	protected T onUpdate(@NonNull T entity, @NonNull Q request) {
		return entity;
	}

	protected void onDelete(@NonNull ID id) {
	}
}
