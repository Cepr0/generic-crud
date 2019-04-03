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
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.List;

public class AbstractCrudController<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {

	protected final CrudService<T, ID, Q, S> service;

	public AbstractCrudController(@NonNull final CrudService<T, ID, Q, S> service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@NonNull
	public S create(@NonNull final Q request) {
		return service.create(request);
	}

	@NonNull
	public ResponseEntity<S> update(@NonNull final ID id, @NonNull final Q request) {
		return ResponseEntity.of(service.update(id, request));
	}

	@NonNull
	public ResponseEntity delete(@NonNull final ID id) {
		if (service.delete(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@NonNull
	public ResponseEntity<S> getOne(@NonNull final ID id) {
		return ResponseEntity.of(service.getOne(id));
	}

	@NonNull
	public Page<S> getAll(@NonNull final Pageable pageable) {
		return service.getAll(pageable);
	}

	@NonNull
	public List<S> getAll(@NonNull final Sort sort) {
		return service.getAll(sort);
	}

	@NonNull
	public List<S> getAll() {
		return service.getAll();
	}
}
