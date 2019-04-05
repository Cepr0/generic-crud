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

package io.github.cepr0.crud.dto;

import org.springframework.lang.NonNull;

import java.io.Serializable;

/**
 * Marker interface for all output (response) DTO, related to the persisted entities which CRUD operations are used for.
 *
 * @param <ID> a related entity identifier type
 *
 * @author Serhei Poznanski
 */
public interface CrudResponse<ID extends Serializable> extends Serializable {
	/**
	 * Returns the identifier of the related persisted entity.
	 *
	 * @return the entity identifier, never be {@code null}.
	 */
	@NonNull ID getId();
}
