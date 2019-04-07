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
import io.github.cepr0.demo.model.Car;
import io.github.cepr0.demo.person.CarService;
import io.github.cepr0.demo.person.dto.CarRequest;
import io.github.cepr0.demo.person.dto.CarResponse;
import io.github.cepr0.demo.person.dto.Views;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cars")
public class CarController extends AbstractCrudController<Car, UUID, CarRequest, CarResponse> {

	public CarController(@NonNull final CarService service) {
		super(service);
	}

	@JsonView(Views.ForCar.class)
	@PostMapping
	@Override
	public ResponseEntity<CarResponse> create(@Validated(OnCreate.class) @RequestBody @NonNull final CarRequest request) {
		return super.create(request);
	}

	@JsonView(Views.ForCar.class)
	@PatchMapping("/{id}")
	@Override
	public ResponseEntity<CarResponse> update(@PathVariable("id") @NonNull final UUID id, @Validated(OnUpdate.class) @RequestBody @NonNull final CarRequest request) {
		return super.update(id, request);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity<?> delete(@PathVariable("id") @NonNull final UUID id) {
		return super.delete(id);
	}

	@JsonView(Views.ForCar.class)
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<CarResponse> getOne(@PathVariable("id") @NonNull final UUID id) {
		return super.getOne(id);
	}

	@JsonView(Views.ForCar.class)
	@GetMapping
	@Override
	public ResponseEntity<List<CarResponse>> getAll() {
		return super.getAll();
	}
}
