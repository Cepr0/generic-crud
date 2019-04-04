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

package io.github.cepr0.crud.model;

import java.io.Serializable;

/**
 * Base interface used in the library to mark entities that are identifiable by an ID of any type.
 * <p>
 * You have to inherit your entities from this interface
 * if you want them to use the functionality of this library.
 *
 * @param <ID> type of the entity identifier
 *
 * @author Sergei Poznanski
 */
public interface IdentifiableEntity<ID extends Serializable> extends Serializable {
	/**
	 * Returns the id identifying the entity.
	 *
	 * @return the identifier of the entity or {@code null} if not available.
	 */
	ID getId();
}
