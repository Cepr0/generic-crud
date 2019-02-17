package com.github.cepr0.crud.controller;

import com.github.cepr0.crud.dto.CrudRequest;
import com.github.cepr0.crud.dto.CrudResponse;
import com.github.cepr0.crud.model.IdentifiableEntity;
import com.github.cepr0.crud.service.CrudService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractCrudController<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {

	protected final CrudService<T, ID, Q, S> service;

	public AbstractCrudController(@NonNull final CrudService<T, ID, Q, S> service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.CREATED)
	public S create(@Validated(OnCreate.class) @NonNull final Q request) {
		return service.create(request);
	}

	public ResponseEntity<?> update(@NonNull final ID id, @Validated(OnUpdate.class) @NonNull final Q request) {
		return ResponseEntity.of(service.update(id, request));
	}

	public ResponseEntity<?> delete(@NonNull final ID id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	public ResponseEntity<?> getOne(@PathVariable("id") @NonNull final ID id) {
		return ResponseEntity.of(service.getOne(id));
	}

	public List<S> getAll(final @NonNull Pageable pageable) {
		return service.getAll(pageable);
	}

	public List<S> getAll(final @NonNull Sort sort) {
		return service.getAll(sort);
	}

	public List<S> getAll() {
		return service.getAll();
	}
}
