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

package io.github.cepr0.crud.service;

import io.github.cepr0.crud.dto.ModelRequest;
import io.github.cepr0.crud.dto.ModelResponse;
import io.github.cepr0.crud.model.IntIdEntity;
import io.github.cepr0.crud.model.Model;
import io.github.cepr0.crud.repo.ModelRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@ActiveProfiles("test")
public class CrudServiceTest {

	@Autowired private ModelService modelService;
	@Autowired private ModelRepo modelRepo;

	private Model model1, model2;
	private ModelResponse modelResponse1, modelResponse2;

	@Before
	public void setUp() {
		model1 = (Model) new Model("text1", 1).setId(1).setVersion(0);
		model2 = (Model) new Model("text2", 2).setId(2).setVersion(0);
		modelResponse1 = ModelResponse.builder().id(1).text("text1").number(1).build();
		modelResponse2 = ModelResponse.builder().id(2).text("text2").number(2).build();
	}

	@Sql(statements = "delete from models")
	@Test
	public void create() {
		Model model = new Model("text1", 1);
		ModelRequest modelRequest = new ModelRequest().setText("text2").setNumber(2);

		assertThat(modelService.create(model)).isEqualToIgnoringGivenFields(model1, "id");
		assertThat(modelService.create(modelRequest)).isEqualToIgnoringGivenFields(modelResponse2, "id");

		List<Model> models = modelRepo.findAll(Sort.by("id"));
		assertThat(models).hasSize(2);
		assertThat(models.get(0)).isEqualToIgnoringGivenFields(model1, "id");
		assertThat(models.get(1)).isEqualToIgnoringGivenFields(model2, "id");
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void updated() {
		Model model = new Model("updated", null);
		ModelRequest modelRequest = new ModelRequest().setText("updated");

		Model expectedModel1 = (Model) new Model("updated", 1).setId(1).setVersion(1);
		Model expectedModel2 = (Model) new Model("updated", 2).setId(2).setVersion(1);

		assertThat(modelService.update(1, model))
				.isNotEmpty()
				.get()
				.satisfies(m -> assertThat(m).isEqualToComparingFieldByField(expectedModel1));

		assertThat(modelService.update(2, modelRequest))
				.isNotEmpty()
				.contains(ModelResponse.builder().id(2).text("updated").number(2).build());

		List<Model> models = modelRepo.findAll(Sort.by("id"));
		assertThat(models).hasSize(2);
		assertThat(models.get(0)).isEqualToComparingFieldByField(expectedModel1);
		assertThat(models.get(1)).isEqualToComparingFieldByField(expectedModel2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void delete() {
		boolean result = modelService.delete(1);
		assertThat(result).isTrue();
		assertThat(modelRepo.findAll()).isEmpty();
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void getOne() {
		assertThat(modelService.getOne(1))
				.isNotEmpty()
				.get().satisfies(modelResponse -> assertThat(modelResponse).isEqualToComparingFieldByField(modelResponse1));
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void getOneT() {
		assertThat(modelService.getOneT(1))
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(model1));
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllT() {
		List<Model> models = modelService.getAllT();
		assertThat(models).hasSize(2);
		assertThat(models).contains(model1, model2);
		List<Model> sortedModels = models.stream()
				.sorted(Comparator.comparingInt(IntIdEntity::getId))
				.collect(Collectors.toList());
		assertThat(sortedModels.get(0)).isEqualToComparingFieldByField(model1);
		assertThat(sortedModels.get(1)).isEqualToComparingFieldByField(model2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAll() {
		List<ModelResponse> responses = modelService.getAll();
		assertThat(responses).hasSize(2);
		assertThat(responses).contains(modelResponse1, modelResponse2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllTPaged() {
		List<Model> models = modelService.getAllT(PageRequest.of(0, 1, Sort.by("id"))).getContent();
		assertThat(models).hasSize(1);
		assertThat(models).contains(model1);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model1);

		models = modelService.getAllT(PageRequest.of(1, 1, Sort.by("id"))).getContent();
		assertThat(models).hasSize(1);
		assertThat(models).contains(model2);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllPaged() {
		List<ModelResponse> responses = modelService.getAll(PageRequest.of(0, 1, Sort.by("id"))).getContent();
		assertThat(responses).hasSize(1);
		assertThat(responses).contains(modelResponse1);

		responses = modelService.getAll(PageRequest.of(1, 1, Sort.by("id"))).getContent();
		assertThat(responses).hasSize(1);
		assertThat(responses).contains(modelResponse2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllTSorted() {
		List<Model> models = modelService.getAllT(Sort.by("id"));
		assertThat(models).hasSize(2);
		assertThat(models).containsExactly(model1, model2);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model1);
		assertThat(models.get(1)).isEqualToComparingFieldByField(model2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllSorted() {
		List<ModelResponse> responses = modelService.getAll(Sort.by("id"));
		assertThat(responses).hasSize(2);
		assertThat(responses).containsExactly(modelResponse1, modelResponse2);
	}

	@Configuration
	@EnableJpaRepositories("io.github.cepr0.crud.repo")
	@EntityScan("io.github.cepr0.crud.model")
	@Import(ModelService.class)
	@ComponentScan("io.github.cepr0.crud.mapper")
	public static class Config {
	}
}
