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
