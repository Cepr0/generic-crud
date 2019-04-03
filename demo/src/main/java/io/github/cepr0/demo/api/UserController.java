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

import io.github.cepr0.crud.api.AbstractCrudController;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.api.OnUpdate;
import io.github.cepr0.demo.model.User;
import io.github.cepr0.demo.user.UserService;
import io.github.cepr0.demo.user.dto.UserRequest;
import io.github.cepr0.demo.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends AbstractCrudController<User, Long, UserRequest, UserResponse> {

	public UserController(@NonNull final UserService service) {
		super(service);
	}

	@PostMapping
	@Override
	public UserResponse create(@Validated(OnCreate.class) @RequestBody @NonNull final UserRequest request) {
		return super.create(request);
	}

	@PatchMapping("/{id}")
	@Override
	public ResponseEntity<UserResponse> update(@PathVariable("id") @NonNull final Long id, @Validated(OnUpdate.class) @RequestBody @NonNull final UserRequest request) {
		return super.update(id, request);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity delete(@PathVariable("id") @NonNull final Long id) {
		return super.delete(id);
	}

	@GetMapping("/{id}")
	@Override
	public ResponseEntity<UserResponse> getOne(@PathVariable("id") @NonNull final Long id) {
		return super.getOne(id);
	}

	@GetMapping
	@Override
	public Page<UserResponse> getAll(Pageable pageable) {
		return super.getAll(pageable);
	}
}
