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

package io.github.cepr0.crud.support;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class CrudUtilsTest {

	@Test
	public void copyNonNullProperties() {
		String[] ignoreProperties = new String[]{"id", "version", "createdAt", "updatedAt"};

		Model source = Model.builder()
				.id(1)
				.version(0)
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.text("text")
				.build();
		Model target = Model.builder().build();

		Model result = CrudUtils.copyNonNullProperties(source, target, ignoreProperties);

		System.out.println(result);

		assertThat(result).isEqualTo(target);
		assertThat(result.getId()).isNull();
		assertThat(result.getVersion()).isNull();
		assertThat(result.getCreatedAt()).isNull();
		assertThat(result.getUpdatedAt()).isNull();
		assertThat(result.getText()).isEqualTo("text");

		Model2 source2 = Model2.builder()
				.text("updated")
				.build();

		Model2 target2 = Model2.builder()
				.id(1)
				.version(0)
				.text("text")
				.build();

		Model2 result2 = CrudUtils.copyNonNullProperties(source2, target2, ignoreProperties);

		System.out.println(result2);

		assertThat(result2).isEqualTo(target2);
		assertThat(result2.getId()).isEqualTo(1);
		assertThat(result2.getVersion()).isEqualTo(0);
		assertThat(result2.getText()).isEqualTo("updated");
	}

	@Test
	public void firstWordOf() {
		assertThat(CrudUtils.firstWordOf("SplitCamelCase"))
				.isEqualTo("split");

		assertThat(CrudUtils.firstWordOf("10SplitCamelCase"))
				.isEqualTo("10");

		assertThat(CrudUtils.firstWordOf("splitCamelCase"))
				.isEqualTo("split");
	}

	@Test
	public void toSnakeCase() {
		assertThat(CrudUtils.toSnakeCase("SplitCamelCase"))
				.isEqualTo("split_camel_case");

		assertThat(CrudUtils.toSnakeCase("10SplitCamelCase"))
				.isEqualTo("10_split_camel_case");

		assertThat(CrudUtils.toSnakeCase("splitCamelCase"))
				.isEqualTo("split_camel_case");
	}

	@Test
	public void splitCamelCase() {
		assertThat(CrudUtils.splitCamelCase("SplitCamelCase"))
				.containsExactly("Split", "Camel", "Case");

		assertThat(CrudUtils.splitCamelCase("10SplitCamelCase"))
				.containsExactly("10", "Split", "Camel", "Case");

		assertThat(CrudUtils.splitCamelCase("Split10CamelCase"))
				.containsExactly("Split10", "Camel", "Case");

		assertThat(CrudUtils.splitCamelCase("SplitCamelCase10"))
				.containsExactly("Split", "Camel", "Case10");
	}

	@Data
	@Builder
	private static class Model {
		private Integer id;
		private Integer version;
		private Instant createdAt;
		private Instant updatedAt;
		private String text;
	}

	@Data
	@Builder
	private static class Model2 {
		private Integer id;
		private Integer version;
		private String text;
	}
}