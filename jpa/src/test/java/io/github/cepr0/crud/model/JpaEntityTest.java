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