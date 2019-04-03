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
