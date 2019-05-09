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

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergei Poznanski
 */
public class CallbackMapperTest {

	private CallbackMapper<String, String> mapper;
	private String source, target;

	@Before
	public void setUp() {
		source = "source";
		target = "target";
		mapper = new CallbackMapper<>(this::map, this::callback);
	}

	@Test
	public void apply() {
		assertThat(mapper.apply(source, target)).isEqualTo(this.source + "/" + this.target);
	}

	private String map(String source, String target) {
		return source + "/" + target;
	}

	private void callback(String source, String target) {
		assertThat(source).isEqualTo(this.source);
		assertThat(target).isEqualTo(this.source + "/" + this.target);
	}
}