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

package io.github.cepr0.demo.person;

import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.demo.model.Car;
import io.github.cepr0.demo.person.dto.CarRequest;
import io.github.cepr0.demo.person.dto.CarResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Sergei Poznanski
 */
@Service
public class CarService extends AbstractCrudService<Car, UUID, CarRequest, CarResponse> {
	public CarService(final CarRepo repo, final CarMapper mapper) {
		super(repo, mapper);
	}
}
