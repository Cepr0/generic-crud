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

package io.github.cepr0.crud.mapper;

import io.github.cepr0.crud.model.IdentifiableEntity;
import io.github.cepr0.crud.repo.JpaRepo;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Mapper between the entity id and the persisted entity or its reference (see method {@link SimpleJpaRepository#getOne}).<br/>
 * You can use it in your {@link CrudMapper}s to provide them with the necessary methods (see {@code PersonMapper}
 * and {@code CarMapper} in the {@code demo} module).<br/><br/>
 * It's also a functional interface so you can simple instantiate it in your code with lambda. For example:
 * <pre>
 *    {@code ReferenceMapper<User, Long> refMapper = () -> repo;}
 * </pre>
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <ID> type of the entity identifier
 *
 * @author Sergei Poznanski
 */
@FunctionalInterface
public interface ReferenceMapper<T extends IdentifiableEntity<ID>, ID extends Serializable> {

	/**
	 * Getter of related {@link JpaRepo}
	 *
	 * @return related {@link JpaRepo}
	 */
	@NonNull
	JpaRepo<T, ID> getRepo();

	/**
	 * Maps the entity id to its reference.
	 *
	 * @param id must not be {@code null}
	 * @return reference to the entity
	 */
	@NonNull
	default T toReference(@NonNull ID id) {
		Objects.requireNonNull(id, "The id must not be null!");
		return getRepo().getOne(id);
	}

	/**
	 * Maps the entity to its id.
	 *
	 * @param entity must not be {@code null}
	 * @return the entity id
	 */
	@NonNull
	default ID toId(@NonNull T entity) {
		Objects.requireNonNull(entity, "The entity must not be null!");
		return Objects.requireNonNull(entity.getId(), "The entity id must not be null!");
	}

	/**
	 * Maps a collection of ids to the set of related references.
	 *
	 * @param ids must not be {@code null}
	 * @return the set of related references
	 */
	@NonNull
	default Set<T> toRefSet(@NonNull Collection<ID> ids) {
		Objects.requireNonNull(ids, "The 'ids' must not be null!");
		return ids.stream().map(this::toReference).collect(toSet());
	}

	/**
	 * Maps a collection of entities to the set of their ids.
	 *
	 * @param entities must not be {@code null}
	 * @return the set ids
	 */
	@NonNull
	default Set<ID> toIdSet(@NonNull Collection<T> entities) {
		Objects.requireNonNull(entities, "The entities must not be null!");
		return entities.stream().map(this::toId).filter(Objects::nonNull).collect(toSet());
	}

	/**
	 * Maps a collection of ids to the list of related references.
	 *
	 * @param ids must not be {@code null}
	 * @return the list of related references
	 */
	@NonNull
	default List<T> toRefList(@NonNull Collection<ID> ids) {
		Objects.requireNonNull(ids, "The 'ids' must not be null!");
		return ids.stream().map(this::toReference).collect(toList());
	}

	/**
	 * Maps a collection of entities to the list of their ids.
	 *
	 * @param entities must not be {@code null}
	 * @return the list ids
	 */
	@NonNull
	default List<ID> toIdList(@NonNull Collection<T> entities) {
		Objects.requireNonNull(entities, "The entities must not be null!");
		return entities.stream().map(this::toId).filter(Objects::nonNull).collect(toList());
	}
}
