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
import io.github.cepr0.demo.model.Person;
import io.github.cepr0.demo.person.dto.PersonRequest;
import io.github.cepr0.demo.person.dto.PersonResponse;
import io.github.cepr0.demo.person.event.UpdatePersonEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Service with example of async post-processing of updating the entity.
 *
 * @author Sergei Poznanski
 */
@Slf4j
@Service
public class PersonService extends AbstractCrudService<Person, UUID, PersonRequest, PersonResponse> {
	public PersonService(final PersonRepo repo, final PersonMapper mapper) {
		super(repo, mapper);
	}

	@Override
	protected UpdatePersonEvent onUpdateEvent(final Person entity) {
		return new UpdatePersonEvent(entity);
	}

	@Async
	@SneakyThrows
	@TransactionalEventListener
	public void handleUpdatePersonEvent(UpdatePersonEvent event) {
		log.info("[i] Person update event after commit handling...");
		TimeUnit.SECONDS.sleep(1); // simulate the delay
		Person person = event.getEntity();
		Set<Car> cars = person.getCars();
		log.info("[i] Person cars: {}", cars);
	}
}
