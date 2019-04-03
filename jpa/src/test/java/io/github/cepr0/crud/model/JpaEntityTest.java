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

package io.github.cepr0.crud.model;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class JpaEntityTest {

	private Model model1;
	private Model model2;

	@Before
	public void setUp() throws Exception {
		model1 = new Model("model1", 1);
		model2 = new Model("model2", 2);
	}

	@Test
	public void set() {
		HashSet<Model> models = new HashSet<>(asList(model1, model2));
		Assertions.assertThat(models).hasSize(2);
	}

	@Test
	public void equals() {
		assertThat(model1).isNotEqualTo(model2);
		model1.setId(1);
		model2.setId(1);
		assertThat(model1).isEqualTo(model2);
	}
}