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

package io.github.cepr0.crud.repo;

import com.integralblue.log4jdbc.spring.Log4jdbcAutoConfiguration;
import io.github.cepr0.crud.model.Model;
import io.github.cepr0.crud.support.CrudUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
@ActiveProfiles("test")
@Import(Log4jdbcAutoConfiguration.class)
public class JpaRepoTest {

	@Autowired private ModelRepo modelRepo;

	private Model model1, model2;

	@Before
	public void setUp() throws Exception {
		model1 = (Model) new Model("text1", 1).setId(1).setVersion(0);
		model2 = (Model) new Model("text2", 2).setId(2).setVersion(0);
	}

	@Sql(statements = "delete from models")
	@Test
	public void create() {
		Model model = new Model("text1", 1);
		modelRepo.create(model);

		List<Model> models = modelRepo.findAll();
		assertThat(models).hasSize(1);
		assertThat(models.get(0)).isEqualToIgnoringGivenFields(model1, "id");

		assertThatNullPointerException()
				.isThrownBy(() -> modelRepo.create(null))
				.withMessage("The given entity must not be null!");
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void updated() {
		BiFunction<Model, Model, Model> mapper = (s, t) ->
				CrudUtils.copyNonNullProperties(s, t, "id", "version", "createdAt", "updatedAt");
		Model source = new Model("updated", null);

		Optional<Model> optionalModel = modelRepo.update(1, source, mapper);

		source.setNumber(1).setId(1).setVersion(1);
		assertThat(optionalModel)
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(source));

		List<Model> models = modelRepo.findAll();
		assertThat(models).hasSize(1);
		assertThat(models.get(0)).isEqualToComparingFieldByField(source);

		modelRepo.update(null, source, mapper);

		assertThatNullPointerException()
				.isThrownBy(() -> modelRepo.update(1, null, mapper))
				.withMessage("The given source must not be null!");

		assertThatNullPointerException().isThrownBy
				(() -> modelRepo.update(1, source, null))
				.withMessage("The given mapper must not be null!");
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void delete() {
		Optional<Model> optionalModel = modelRepo.delete(1);

		assertThat(optionalModel)
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(model1));

		assertThat(modelRepo.findAll()).isEmpty();
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models (id, version, text, number) values (1, 0, 'text1', 1)")
	@Test
	public void getById() {
		assertThat(modelRepo.getById(1))
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(model1));
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAll() {
		List<Model> models = modelRepo.getAll();
		assertThat(models).hasSize(2);
		assertThat(models).contains(model1, model2);
		assertThat(models.stream().filter(model -> model.getId() != null && model.getId().equals(1)).findFirst())
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(model1));
		assertThat(models.stream().filter(model -> model.getId() != null && model.getId().equals(2)).findFirst())
				.isNotEmpty()
				.get().satisfies(model -> assertThat(model).isEqualToComparingFieldByField(model2));
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllPaged() {
		List<Model> models = modelRepo.getAll(PageRequest.of(0, 1, Sort.by("id"))).getContent();
		assertThat(models).hasSize(1);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model1);

		models = modelRepo.getAll(PageRequest.of(1, 1, Sort.by("id"))).getContent();
		assertThat(models).hasSize(1);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model2);
	}

	@Sql(statements = "delete from models")
	@Sql(statements = "insert into models(id, version, text, number) values (1, 0, 'text1', 1), (2, 0, 'text2', 2)")
	@Test
	public void getAllSorted() {
		List<Model> models = modelRepo.getAll(Sort.by("id"));
		assertThat(models).hasSize(2);
		assertThat(models).containsExactly(model1, model2);
		assertThat(models.get(0)).isEqualToComparingFieldByField(model1);
		assertThat(models.get(1)).isEqualToComparingFieldByField(model2);
	}

	@Configuration
	@EnableJpaRepositories
	@EntityScan("io.github.cepr0.crud.model")
	public static class Config {
	}
}