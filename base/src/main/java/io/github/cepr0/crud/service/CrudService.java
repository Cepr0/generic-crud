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

public interface CrudService<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {

	@NonNull S create(@NonNull Q source);
	@NonNull T create(@NonNull T source);

	@NonNull Optional<S> update(@NonNull ID id, @NonNull Q source);
	@NonNull Optional<T> update(@NonNull ID id, @NonNull T source);

	@NonNull boolean delete(@NonNull ID id);

	@NonNull Optional<S> getOne(@NonNull ID id);
	@NonNull Optional<T> getOneT(@NonNull ID id);

	@NonNull List<S> getAll();
	@NonNull List<T> getAllT();

	@NonNull Page<S> getAll(@NonNull Pageable pageable);
	@NonNull Page<T> getAllT(@NonNull Pageable pageable);

	@NonNull List<S> getAll(@NonNull Sort sort);
	@NonNull List<T> getAllT(@NonNull Sort sort);
}
