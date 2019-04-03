package io.github.cepr0.crud.repo;

import io.github.cepr0.crud.mapper.BeanMapper;
import io.github.cepr0.crud.model.IdentifiableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> {
	@NonNull T create(@NonNull T entity);

	@NonNull Optional<T> getToUpdateById(@NonNull ID id);
	@NonNull <S> Optional<T> update(@NonNull ID id, @NonNull S source, @NonNull BeanMapper<S, T> mapper);

	@NonNull Optional<T> getToDeleteById(@NonNull ID id);
	@NonNull Optional<T> delete(@NonNull ID id);

	@NonNull Optional<T> getById(@NonNull ID id);

	@NonNull List<T> getAll();
	@NonNull Page<T> getAll(@NonNull Pageable pageable);
	@NonNull List<T> getAll(@NonNull Sort sort);
}
