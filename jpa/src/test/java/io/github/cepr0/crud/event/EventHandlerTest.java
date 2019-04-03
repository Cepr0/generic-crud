package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.Event;
import io.github.cepr0.crud.model.Model;
import io.github.cepr0.crud.service.ModelService;
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

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@ActiveProfiles("test")
public class EventHandlerTest {

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
	@EnableJpaRepositories({"io.github.cepr0.crud.repo", "io.github.cepr0.crud.event"})
	@EntityScan("io.github.cepr0.crud.model")
	@Import({EventHandler.class, ModelService.class})
	@ComponentScan("io.github.cepr0.crud.mapper")
	public static class Config {
	}
}
