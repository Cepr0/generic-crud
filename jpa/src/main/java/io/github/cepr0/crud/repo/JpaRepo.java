package io.github.cepr0.crud.repo;

import io.github.cepr0.crud.mapper.BeanMapper;
import io.github.cepr0.crud.model.IdentifiableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Transactional
@NoRepositoryBean
public interface JpaRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> extends CrudRepo<T, ID>, JpaRepository<T, ID> {

	@NonNull
	@Override
	default T create(@NonNull final T entity) {
		return save(entity);
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	Optional<T> getToUpdateById(@NonNull ID id);

	@NonNull
	@Override
	default <S> Optional<T> update(@NonNull final ID id, @NonNull final S source, @NonNull final BeanMapper<S, T> mapper) {
		return getToUpdateById(id)
				.map(target -> mapper.map(source, target))
				.map(target -> {
					flush();
					return target;
				});
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	Optional<T> getToDeleteById(@NonNull ID id);

	@Override
	default Optional<T> delete(@NonNull final ID id) {
		return getToDeleteById(id).map(found -> {
			delete(found);
			return found;
		});
	}

	@Transactional(readOnly = true)
	@NonNull
	@Override
	Optional<T> getById(@NonNull ID id);

	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<T> getAll();

	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	Page<T> getAll(@NonNull Pageable pageable);

	@Transactional(readOnly = true)
	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<T> getAll(@NonNull Sort sort);
}
