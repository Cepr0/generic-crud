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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergei Poznanski
 */
public class JpaEntityTest {

	private Model x;
	private Model y;
	private Model z;

	@Before
	public void setUp() {
		x = new Model(1);
		y = new Model(1);
		z = new Model(1);
	}

	@Test
	public void set() {
		Model model1 = new Model(null);
		Model model2 = new Model(null);

		HashSet<Model> models = new HashSet<>(asList(model1, model2));
		Assertions.assertThat(models).hasSize(2);
	}

	@Test
	public void equals() {
		Model model1 = new Model(null);
		Model model2 = new Model(null);

		assertThat(model1).isNotEqualTo(model2);

		model1.setId(1);
		model2.setId(1);
		assertThat(model1).isEqualTo(model2);
	}

	@SuppressWarnings("EqualsWithItself")
	@Test
	public void reflexive() {
		assertThat(x.equals(x)).isTrue();
	}

	@Test
	public void symmetric() {
		assertThat(x.equals(y)).isTrue();
		assertThat(y.equals(x)).isTrue();
	}

	@Test
	public void transitive() {
		assertThat(x.equals(y)).isTrue();
		assertThat(y.equals(z)).isTrue();
		assertThat(z.equals(x)).isTrue();
	}

	@Test
	public void consistence() {
		for (int i = 0; i < 1000; i++) {
			assertThat(x.equals(y)).isTrue();
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void nullComparison() {
		assertThat(new Model(null).equals(null)).isFalse();
		assertThat(new Model(0).equals(null)).isFalse();
		assertThat(new Model(Integer.MIN_VALUE).equals(null)).isFalse();
		assertThat(new Model(Integer.MAX_VALUE).equals(null)).isFalse();
	}

	@Test
	public void hashCodeContract() {
		assertThat(x.equals(y)).isTrue();
		assertThat(x.hashCode()).isEqualTo(y.hashCode());

		// consistence
		assertThat(x.hashCode()).isEqualTo(x.hashCode());
	}

	@Test
	public void comparisonWithProxy() {
		Model model = new Model(1);
		Model proxy = (Model) Enhancer.create(
				Model.class,
				(MethodInterceptor) (object, method, args, methodProxy) -> methodProxy.invoke(model, args)
		);
		assertThat(model.equals(proxy)).isTrue();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	static class Model extends JpaEntity<Integer> {
		private Integer id;
	}
}