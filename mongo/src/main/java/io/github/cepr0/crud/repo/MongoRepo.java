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
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.lang.String.format;

/**
 * Implementation of {@link CrudRepo} which extends {@link MongoRepository} and all it functionality.
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 *
 * @author Sergei Poznanski
 */
@NoRepositoryBean
public interface MongoRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> extends CrudRepo<T, ID>, MongoRepository<T, ID> {
	@Override
	@NonNull
	default T create(@NonNull final T entity) {
		Objects.requireNonNull(entity, "The given entity must not be null!");
		return insert(entity);
	}

	/**
	 * Retrieves an entity to be updated, by its id.
	 * Used in the {@link MongoRepo#update} method.
	 *
	 * @param id must not be {@code null}
	 * @return the entity with the given id or {@code Optional#empty()} if none found
	 */
	@NonNull
	Optional<T> getToUpdateById(@NonNull ID id);

	@Override
	@NonNull
	default <S> Optional<T> update(@NonNull final ID id, S source, @NonNull final BiFunction<S, T, T> mapper) {
		Objects.requireNonNull(source, "The given source must not be null!");
		Objects.requireNonNull(mapper, "The given mapper must not be null!");
		return getToUpdateById(id)
				.map(target -> save(mapper.apply(source, target)));
	}

	/**
	 * Retrieves an entity to be deleted, by its id.
	 * Used in {@link MongoRepo#del} method.
	 *
	 * @param id must not be {@code null}
	 * @return the entity with the given id or {@code Optional#empty()} if none found
	 */
	@NonNull
	Optional<T> getToDeleteById(@NonNull ID id);

	@Override
	@NonNull
	default Optional<T> del(@NonNull ID id) {
		return getToDeleteById(id).map(found -> {
			delete(found);
			return found;
		});
	}

	@Override
	@NonNull
	Optional<T> getById(@NonNull ID id);

	/**
	 * Returns an object with the given identifier. Throws a {@link DocNotFoundException} if the object is not found.
	 *
	 * @param id must not be {@code null}
	 * @return an object with the given identifier
	 */
	@NonNull
	default T getOne(@NonNull ID id) {
		return findById(id)
				.orElseThrow(() -> new DocNotFoundException(format("Mongo document with id '%s' not found", id)));
	}

	@Query("{id: { $exists: true }}")
	@Override
	@NonNull
	List<T> getAll();

	@Query("{id: { $exists: true }}")
	@Override
	@NonNull
	Page<T> getAll(@NonNull Pageable pageable);

	@Query("{id: { $exists: true }}")
	@Override
	@NonNull
	List<T> getAll(@NonNull Sort sort);
}
