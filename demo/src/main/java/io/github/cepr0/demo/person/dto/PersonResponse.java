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

package io.github.cepr0.demo.person.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.github.cepr0.crud.dto.CrudResponse;
import io.github.cepr0.crud.model.ContentAlias;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

/**
 * @author Sergei Poznanski
 */
@ContentAlias("people")
@Data
public class PersonResponse implements CrudResponse<UUID> {
	@JsonView(Views.ForPerson.class) private UUID id;
	@JsonView(Views.ForPerson.class) private String name;
	@JsonView(Views.ForPerson.class) private Set<CarResponse> cars;
}
