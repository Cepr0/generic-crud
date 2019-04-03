package io.github.cepr0.crud.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.cepr0.crud.model.ContentAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CrudPageSerializerTest {

	private Writer writer;
	private JsonGenerator generator;
	private SerializerProvider provider;
	private ObjectMapper mapper;

	private CrudPageSerializer serializer;

	private String contentBlock = "tests";
	private String pageBlock = "page";
	private String elementsBlock = "elements";

	private String pageNumber = "number";
	private String pageSize = "size";
	private String pageTotal = "total";
	private String pageFirst = "first";
	private String pageLast = "last";
	private String elementsTotal = "total";
	private String elementsCurrent = "current";

	@Before
	public void setUp() throws Exception {
		writer = new StringWriter();
		mapper = new ObjectMapper();
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.setCodec(mapper);
		generator = jsonFactory.createGenerator(writer);
		provider = mapper.getSerializerProvider();
	}

	@Test
	public void serializeDefault() throws Exception {
		serializer = new CrudPageSerializer();

		testSerialize(
				new TestResponse().setName("name1"),
				new TestResponse().setName("name2")
		);
	}

	@Test
	public void serializeContentAlias() throws Exception {
		serializer = new CrudPageSerializer();

		contentBlock = "dtos";

		testSerialize(
				new TestDto().setName("name1"),
				new TestDto().setName("name2")
		);
	}

	@Test
	public void serializeWithCamelCase() throws Exception {
		serializer = new CrudPageSerializer() {{
			contentAliasMode = ContentAliasMode.CAMEL_CASE;
		}};

		contentBlock = "TestResponses";

		testSerialize(
				new TestResponse().setName("name1"),
				new TestResponse().setName("name2")
		);
	}

	@Test
	public void serializeWithSnakeCase() throws Exception {
		serializer = new CrudPageSerializer() {{
			contentAliasMode = ContentAliasMode.SNAKE_CASE;
		}};

		contentBlock = "test_responses";

		testSerialize(
				new TestResponse().setName("name1"),
				new TestResponse().setName("name2")
		);
	}

	@Test
	public void serializeWithDefaultContentNameAndCustomFieldNames() throws Exception {
		serializer = new CrudPageSerializer() {{

			contentAliasMode = ContentAliasMode.DEFAULT_NAME;

			defaultContentName = "custom_content";

			pageBlock = "custom_page";
			pageNumber = "custom_number";
			pageSize = "custom_size";
			pageTotal = "custom_total";
			pageFirst = "custom_first";
			pageLast = "custom_last";

			elementsBlock = "custom_elements";
			elementsTotal = "custom_total";
			elementsOnPage = "custom_current";

			sortBlock = "custom_sort";
			sortedProperty = "custom_property";
			sortedDirection = "custom_direction";

		}};

		contentBlock = serializer.defaultContentName;
		pageBlock = serializer.pageBlock;
		elementsBlock = serializer.elementsBlock;

		pageNumber = serializer.pageNumber;
		pageSize = serializer.pageSize;
		pageTotal = serializer.pageTotal;
		pageFirst = serializer.pageFirst;
		pageLast = serializer.pageLast;

		elementsTotal = serializer.elementsTotal;
		elementsCurrent = serializer.elementsOnPage;

		testSerialize(
				new TestResponse().setName("name1"),
				new TestResponse().setName("name2")
		);
	}


	private void testSerialize(Object... elements) throws IOException {
		Assert.notEmpty(elements, "Arg array must not be empty");

		List<Object> content = asList(elements);
		Page<Object> page = new PageImpl<>(content);

		serializer.serialize(page, generator, provider);
		generator.flush();
		String json = writer.toString();

		JsonNode rootNode = mapper.readTree(json);
		assertThat(rootNode.fieldNames()).containsOnly(contentBlock, pageBlock, elementsBlock);

		JsonNode contentNode = rootNode.get(contentBlock);
		assertThat(contentNode).hasSize(elements.length);

		for (int i = 0; i < elements.length; i++) {
			assertThat(mapper.treeToValue(contentNode.get(i), elements[i].getClass())).isEqualTo(content.get(i));
		}

		JsonNode pageNode = rootNode.get(pageBlock);
		assertThat(pageNode.fieldNames()).containsOnly(pageNumber, pageSize, pageTotal, pageFirst, pageLast);

		JsonNode elementsNode = rootNode.get(elementsBlock);
		assertThat(elementsNode.fieldNames()).containsOnly(elementsTotal, elementsCurrent);
	}

	@Accessors(chain = true)
	@Data
	private static class TestResponse {
		private String name;
	}

	@ContentAlias("dtos")
	@Accessors(chain = true)
	@Data
	private static class TestDto {
		private String name;
	}
}