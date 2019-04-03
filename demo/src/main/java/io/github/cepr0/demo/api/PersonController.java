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

package io.github.cepr0.demo.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.github.cepr0.crud.api.AbstractCrudController;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.api.OnUpdate;
import io.github.cepr0.demo.model.Person;
import io.github.cepr0.demo.person.PersonService;
import io.github.cepr0.demo.person.dto.PersonRequest;
import io.github.cepr0.demo.person.dto.PersonResponse;
import io.github.cepr0.demo.person.dto.Views;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("people")
public class PersonController extends AbstractCrudController<Person, UUID, PersonRequest, PersonResponse> {

	public PersonController(@NonNull final PersonService service) {
		super(service);
	}

	@JsonView(Views.ForPerson.class)
	@PostMapping
	@Override
	public PersonResponse create(@Validated(OnCreate.class) @RequestBody @NonNull final PersonRequest request) {
		return super.create(request);
	}

	@JsonView(Views.ForPerson.class)
	@PatchMapping("/{id}")
	@Override
	public ResponseEntity<PersonResponse> update(@PathVariable("id") @NonNull final UUID id, @Validated(OnUpdate.class) @RequestBody @NonNull final PersonRequest request) {
		return super.update(id, request);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity delete(@PathVariable("id") @NonNull final UUID id) {
		return super.delete(id);
	}

	@JsonView(Views.ForPerson.class)
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<PersonResponse> getOne(@PathVariable("id") @NonNull final UUID id) {
		return super.getOne(id);
	}

	@JsonView(Views.ForPerson.class)
	@GetMapping
	@Override
	public Page<PersonResponse> getAll(Pageable pageable) {
		return super.getAll(pageable);
	}
}
