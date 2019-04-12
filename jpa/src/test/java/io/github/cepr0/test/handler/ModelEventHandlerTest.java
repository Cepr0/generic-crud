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

package io.github.cepr0.test.handler;

import io.github.cepr0.test.model.Event;
import io.github.cepr0.test.model.Model;
import io.github.cepr0.test.repo.EventRepo;
import io.github.cepr0.test.service.ModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * @author Sergei Poznanski
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@ActiveProfiles("test")
public class ModelEventHandlerTest {

	@Autowired private ModelService modelService;
	@Autowired private EventRepo eventRepo;

	@Sql(statements = "delete from models")
	@Sql(statements = "delete from history")
	@Test
	public void createEvent() {
		Model model = modelService.create(new Model("model", 1));
		List<Event> events = eventRepo.findAll();
		assertThat(events).hasSize(1);
		Event event = events.get(0);
		assertThat(event.getModelId()).isEqualTo(model.getId());
		assertThat(event.getModelText()).isEqualTo(model.getText());
		assertThat(event.getModelNumber()).isEqualTo(model.getNumber());
		assertThat(event.getType()).isEqualTo(Event.Type.CREATE);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "delete from history")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void updateEvent() {
		modelService.update(1, new Model("model", null));
		List<Event> events = eventRepo.findAll();
		assertThat(events).hasSize(1);
		Event event = events.get(0);
		assertThat(event.getModelId()).isEqualTo(1);
		assertThat(event.getModelText()).isEqualTo("model");
		assertThat(event.getModelNumber()).isEqualTo(1);
		assertThat(event.getType()).isEqualTo(Event.Type.UPDATE);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "delete from history")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void deleteEvent() {
		modelService.delete(1);
		List<Event> events = eventRepo.findAll();
		assertThat(events).hasSize(1);
		Event event = events.get(0);
		assertThat(event.getModelId()).isEqualTo(1);
		assertThat(event.getModelText()).isEqualTo("text1");
		assertThat(event.getModelNumber()).isEqualTo(1);
		assertThat(event.getType()).isEqualTo(Event.Type.DELETE);
	}

	@Configuration
	@EnableJpaRepositories("io.github.cepr0.test.repo")
	@EntityScan("io.github.cepr0.test.model")
	@Import({ModelEventHandler.class, ModelService.class})
	@ComponentScan("io.github.cepr0.test.mapper")
	public static class Config {
	}
}
