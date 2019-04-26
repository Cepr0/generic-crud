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

import io.github.cepr0.crud.support.CrudUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Sergei Poznanski
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles("test")
public class MongoRepoTest {

	@Autowired private ModelRepo modelRepo;

	@Test
	public void create() {
		Model model = modelRepo.create(new Model().setName("test"));
		assertThat(modelRepo.findById(model.getId()))
				.hasValueSatisfying(m -> assertThat(m).isEqualToComparingFieldByField(model));
	}

	@Test
	public void update() {
		Model target = modelRepo.save(new Model().setName("test"));
		Model source = new Model().setName("updated");
		assertThat(modelRepo.update(target.getId(), source, (s, t) -> CrudUtils.copyNonNullProperties(s, t, "id")))
				.isPresent()
				.hasValueSatisfying(model -> {
					assertThat(model.getId()).isEqualTo(target.getId());
					assertThat(model.getName()).isEqualTo("updated");
				});
	}

	@Test
	public void del() {
		Model target = modelRepo.save(new Model().setName("test"));
		assertThat(modelRepo.del(target.getId()))
				.isPresent()
				.hasValueSatisfying(model -> {
					assertThat(model.getId()).isEqualTo(target.getId());
					assertThat(model.getName()).isEqualTo("test");
				});
		assertThat(modelRepo.findById(target.getId())).isEmpty();
	}

	@Test
	public void getOne() {
		Model model = modelRepo.save(new Model().setName("test"));
		assertThat(modelRepo.getOne(model.getId()))
				.isEqualToComparingFieldByField(model);
		modelRepo.delete(model);
		assertThatExceptionOfType(DocNotFoundException.class)
				.isThrownBy(() -> modelRepo.getOne(model.getId()))
				.withMessage(format("Mongo document with id '%s' not found", model.getId()));
	}

	@Test
	public void getToUpdateById() {
		Model model = modelRepo.save(new Model().setName("test"));
		assertThat(modelRepo.getToUpdateById(model.getId()))
				.hasValueSatisfying(m -> assertThat(m).isEqualToComparingFieldByField(model));
	}

	@Test
	public void getToDeleteById() {
		Model model = modelRepo.save(new Model().setName("test"));
		assertThat(modelRepo.getToDeleteById(model.getId()))
				.hasValueSatisfying(m -> assertThat(m).isEqualToComparingFieldByField(model));
	}

	@Test
	public void getById() {
		Model model = modelRepo.save(new Model().setName("test"));
		assertThat(modelRepo.getById(model.getId()))
				.hasValueSatisfying(m -> assertThat(m).isEqualToComparingFieldByField(model));
	}

	@Test
	public void getAll() {
		modelRepo.deleteAll();

		List<Model> models = modelRepo.saveAll(asList(
				new Model().setName("test1"),
				new Model().setName("test2"),
				new Model().setName("test3")
		));

		assertThat(modelRepo.getAll())
				.hasSize(3)
				.containsAll(models);
	}

	@Test
	public void getAllSorted() {
		modelRepo.deleteAll();

		List<Model> models = modelRepo.saveAll(asList(
				new Model().setName("test1"),
				new Model().setName("test2"),
				new Model().setName("test3")
		));
		assertThat(modelRepo.getAll(new Sort(Sort.Direction.DESC, "name")))
				.containsExactly(models.get(2), models.get(1), models.get(0));
	}

	@Test
	public void getAllPaged() {
		modelRepo.deleteAll();

		List<Model> models = modelRepo.saveAll(asList(
				new Model().setName("test1"),
				new Model().setName("test2"),
				new Model().setName("test3")
		));
		assertThat(modelRepo.getAll(new PageRequest(0, 2, new Sort(Sort.Direction.DESC,"name"))))
				.containsExactly(models.get(2), models.get(1));
	}

	@Configuration
	@EnableMongoRepositories(basePackageClasses = ModelRepo.class)
	public static class Config {
	}
}