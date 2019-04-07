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

package io.github.cepr0.crud.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.cepr0.crud.service.CrudService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergei Poznanski
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AbstractCrudControllerTest.TestCrudController.class)
public class AbstractCrudControllerTest {

	static final String MODELS = "/models";

	@MockBean private CrudService<Model, Integer, ModelRequest, ModelResponse> modelService;
	@Autowired private MockMvc mvc;
	@Autowired protected ObjectMapper objectMapper;

	private ModelRequest modelRequest;
	private ModelResponse modelResponse1;
	private ModelResponse modelResponse2;
	private Sort sort = Sort.by("id");
	private Pageable pageable = PageRequest.of(0, 20, sort);

	@Before
	public void setUp() {
		modelRequest = new ModelRequest().setName("model1");
		modelResponse1 = new ModelResponse().setId(1).setName("model1");

		modelResponse2 = new ModelResponse().setId(2).setName("model2");
		final List<ModelResponse> responses = new ArrayList<>(asList(modelResponse1, modelResponse2));

		when(modelService.create(modelRequest)).thenReturn(modelResponse1);
		when(modelService.update(1, modelRequest)).thenReturn(Optional.of(modelResponse1));

		when(modelService.delete(1)).thenReturn(true);
		when(modelService.delete(0)).thenReturn(false);

		when(modelService.getOne(1)).thenReturn(Optional.of(modelResponse1));
		when(modelService.getOne(0)).thenReturn(Optional.empty());

		when(modelService.getAll()).thenReturn(responses);
		when(modelService.getAll(sort)).thenReturn(responses);
		when(modelService.getAll(pageable)).thenReturn(new PageImpl<>(responses, pageable, 2));
	}

	private String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	private ModelResponse bodyToObject(ResultActions result) throws Exception {
		String body = result.andReturn().getResponse().getContentAsString();
		return objectMapper.readValue(body, ModelResponse.class);
	}

	private List<ModelResponse> bodyToList(ResultActions result) throws Exception {
		String body = result.andReturn().getResponse().getContentAsString();
		//noinspection unchecked
		Class<ModelResponse[]> arrayClass = (Class<ModelResponse[]>) Class.forName("[L" + ModelResponse.class.getName() + ";");
		return asList(objectMapper.readValue(body, arrayClass));
	}

	@Test
	public void create() throws Exception {
		ResultActions result = mvc.perform(post(MODELS)
				.contentType(APPLICATION_JSON_UTF8)
				.content(toJson(modelRequest))
				.accept(APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));

		assertThat(bodyToObject(result)).isEqualTo(modelResponse1);
	}

	@Test
	public void update() throws Exception {
		ResultActions result = mvc.perform(patch(MODELS + "/{id}", 1)
				.contentType(APPLICATION_JSON_UTF8)
				.content(toJson(modelRequest))
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));

		assertThat(bodyToObject(result)).isEqualTo(modelResponse1);
	}

	@Test
	public void deleteOne() throws Exception {
		mvc.perform(delete(MODELS + "/{id}", 1))
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$").doesNotExist());
		mvc.perform(delete("/tests/{id}", 0))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void getOne() throws Exception {
		ResultActions result = mvc.perform(get(MODELS + "/{id}", 1)
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8));

		assertThat(bodyToObject(result)).isEqualTo(modelResponse1);

		mvc.perform(get("/tests/{id}", 0)
				.accept(APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	public void getAll() throws Exception {
		ResultActions result = mvc.perform(get(MODELS)
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)));

		List<ModelResponse> actual = bodyToList(result);
		assertThat(actual).contains(modelResponse2, modelResponse1);
	}

	@Test
	public void getAllSorted() throws Exception {
		ResultActions result = mvc.perform(get(MODELS + "/all/sorted?sort=id")
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)));

		List<ModelResponse> actual = bodyToList(result);
		assertThat(actual).containsExactly(modelResponse1, modelResponse2);
	}

	@Test
	public void getAllPaged() throws Exception {
		mvc.perform(get(MODELS + "/all/paged?sort=id&page=0&size=20")
				.accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.models", hasSize(2)))
				.andExpect(jsonPath("$.models[0].id", is(1)))
				.andExpect(jsonPath("$.models[1].id", is(2)))
				.andExpect(jsonPath("$.models[0].name", is("model1")))
				.andExpect(jsonPath("$.models[1].name", is("model2")))
				.andExpect(jsonPath("$.page").isNotEmpty())
				.andExpect(jsonPath("$.page.number", is(0)))
				.andExpect(jsonPath("$.page.size", is(20)))
				.andExpect(jsonPath("$.page.first", is(true)))
				.andExpect(jsonPath("$.page.last", is(true)))
				.andExpect(jsonPath("$.elements").isNotEmpty())
				.andExpect(jsonPath("$.elements.total", is(2)))
				.andExpect(jsonPath("$.elements.exposed", is(2)))
				.andExpect(jsonPath("$.sort", hasSize(1)))
				.andExpect(jsonPath("$.sort[0].property", is("id")))
				.andExpect(jsonPath("$.sort[0].direction", is("ASC")));
	}

	@RestController
	@RequestMapping(MODELS)
	static class TestCrudController extends AbstractCrudController<Model, Integer, ModelRequest, ModelResponse> {

		public TestCrudController(CrudService<Model, Integer, ModelRequest, ModelResponse> service) {
			super(service);
		}

		@PostMapping
		@Override
		public ResponseEntity<ModelResponse> create(@RequestBody final ModelRequest request) {
			return super.create(request);
		}

		@PatchMapping("/{id}")
		@Override
		public ResponseEntity<ModelResponse> update(@PathVariable("id") final Integer id, @RequestBody final ModelRequest request) {
			return super.update(id, request);
		}

		@DeleteMapping("/{id}")
		@Override
		public ResponseEntity delete(@PathVariable("id") final Integer id) {
			return super.delete(id);
		}

		@GetMapping("/{id}")
		@Override
		public ResponseEntity<ModelResponse> getOne(@PathVariable("id") final Integer id) {
			return super.getOne(id);
		}

		@GetMapping("/all/paged")
		@Override
		public ResponseEntity<Page<ModelResponse>> getAll(final Pageable pageable) {
			return super.getAll(pageable);
		}

		@GetMapping("/all/sorted")
		@Override
		public ResponseEntity<List<ModelResponse>> getAll(final Sort sort) {
			return super.getAll(sort);
		}

		@GetMapping
		@Override
		public ResponseEntity<List<ModelResponse>> getAll() {
			return super.getAll();
		}
	}

	@Configuration
	@EnableWebMvc
	@Import(AbstractCrudControllerTest.TestCrudController.class)
	public static class TestConfig implements WebMvcConfigurer {
		@Override
		public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
			SimpleModule m = new SimpleModule();
			m.addSerializer(Page.class, new CrudPageSerializer());
			ObjectMapper mapper = new Jackson2ObjectMapperBuilder().modules(m).build();
			converters.add(new MappingJackson2HttpMessageConverter(mapper));
		}
	}
}