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

package io.github.cepr0.crud.repo;

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
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Implementation of {@link CrudRepo} which extends {@link JpaRepository} and all it functionality.
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 * @author Sergei Poznanski
 */
@Transactional
@NoRepositoryBean
public interface JpaRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> extends CrudRepo<T, ID>, JpaRepository<T, ID> {

	@NonNull
	@Override
	default T create(@NonNull final T entity) {
		Objects.requireNonNull(entity, "The given entity must not be null!");
		return save(entity);
	}

	/**
	 * Retrieves an entity to be updated, by its id.
	 * Used in the {@link JpaRepo#update} method.
	 *
	 * @param id must not be {@code null}
	 * @return the entity with the given id or {@code Optional#empty()} if none found
	 */
	@Transactional(readOnly = true)
	@NonNull
	Optional<T> getToUpdateById(@NonNull ID id);

	@NonNull
	@Override
	default <S> Optional<T> update(@NonNull final ID id, @NonNull final S source, @NonNull final BiFunction<S, T, T> mapper) {
		Objects.requireNonNull(source, "The given source must not be null!");
		Objects.requireNonNull(mapper, "The given mapper must not be null!");
		return getToUpdateById(id).map(target -> mapper.apply(source, target));
	}

	/**
	 * Retrieves an entity to be deleted, by its id.
	 * Used in {@link JpaRepo#del} method.
	 *
	 * @param id must not be {@code null}
	 * @return the entity with the given id or {@code Optional#empty()} if none found
	 */
	@Transactional(readOnly = true)
	@NonNull
	Optional<T> getToDeleteById(@NonNull ID id);

	@Override
	default Optional<T> del(@NonNull final ID id) {
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
