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

package io.github.cepr0.test.service;

import io.github.cepr0.crud.event.EntityEvent;
import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.test.dto.ModelRequest;
import io.github.cepr0.test.dto.ModelResponse;
import io.github.cepr0.test.event.CreateModelEvent;
import io.github.cepr0.test.event.DeleteModelEvent;
import io.github.cepr0.test.event.UpdateModelEvent;
import io.github.cepr0.test.mapper.ModelMapper;
import io.github.cepr0.test.model.Model;
import io.github.cepr0.test.repo.ModelRepo;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergei Poznanski
 */
@Service
public class ModelService extends AbstractCrudService<Model, Integer, ModelRequest, ModelResponse> {
	protected ModelService(@NonNull final ModelRepo repo, @NonNull final ModelMapper mapper) {
		super(repo, mapper);
	}

	@Override
	protected EntityEvent<Model> onCreateEvent(final Model entity) {
		return new CreateModelEvent(entity);
	}

	@Override
	protected EntityEvent<Model> onUpdateEvent(final Model entity) {
		return new UpdateModelEvent(entity);
	}

	@Override
	protected EntityEvent<Model> onDeleteEvent(final Model entity) {
		return new DeleteModelEvent(entity);
	}

	@Override
	protected void onCreate(final ModelRequest request, final Model model) {
		assertThat(request).isEqualToComparingFieldByField(new ModelRequest().setText("text2").setNumber(2));
		assertThat(model).isEqualToComparingFieldByField(new Model("text2", 2));
	}

	@Override
	protected void onUpdate(final ModelRequest request, final Model model) {
		assertThat(request).isEqualToIgnoringGivenFields(new ModelRequest().setText("updated"));
		assertThat(model).isEqualToIgnoringGivenFields(new Model("updated", 2).setId(2).setVersion(0));
	}
}
