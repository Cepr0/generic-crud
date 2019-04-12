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

package io.github.cepr0.test.repo;

import io.github.cepr0.crud.repo.JpaRepo;
import io.github.cepr0.test.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergei Poznanski
 */
public interface ModelRepo extends JpaRepo<Model, Integer> {
	@NonNull
	@Override
	Optional<Model> getToUpdateById(@NonNull Integer integer);

	@NonNull
	@Override
	Optional<Model> getToDeleteById(@NonNull Integer integer);

	@NonNull
	@Override
	Optional<Model> getById(@NonNull Integer integer);

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<Model> getAll();

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	Page<Model> getAll(@NonNull Pageable pageable);

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<Model> getAll(@NonNull Sort sort);
}
