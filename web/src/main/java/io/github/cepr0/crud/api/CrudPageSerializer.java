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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.cepr0.crud.model.ContentAlias;
import io.github.cepr0.crud.support.CrudUtils;
import org.atteo.evo.inflector.English;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

/**
 * A customizable {@link Page} serializer. It provides the flowing view:
 * <pre>
 * {
 *     "users": [
 *         {
 *             "id": 1,
 *             "name": "user1"
 *         },
 *         {
 *             "id": 2,
 *             "name": "user2"
 *         }
 *     ],
 *     "page": {
 *         "number": 0,
 *         "size": 20,
 *         "total": 1,
 *         "first": true,
 *         "last": true
 *     },
 *     "elements": {
 *         "total": 2,
 *         "exposed": 2
 *     },
 *     "sort": [
 *         {
 *             "property": "name",
 *             "direction": "ASC"
 *         }
 *     ]
 * }
 * </pre>
 * (where 'users' is "content" field)<br/><br/>
 * <p>
 * You can set custom names of every fields of this view by inheriting this serializer
 * and changing the value of the corresponding protected fields (see the source code of the serializer).
 * <p>
 * To change the plural name of the "content" field (e.g. 'users' in the example above) you can use
 * the {@link ContentAlias} annotation, and place it on the "content" class (e.g. 'UserResponse'),
 * and/or change the value of {@link CrudPageSerializer#contentAliasMode} property.
 * The {@link ContentAlias} value has a higher priority (if it set),
 * then the {@link CrudPageSerializer#contentAliasMode} value is taken into consideration.
 * <p>
 * Note that if the value of "content" or "sort" fields is empty, then these fields are not displayed.
 * <p>
 * To use the serializer you can simple inherit it and register with {@code @JsonComponent} annotation
 * or you can register right this serializer in {@code WebMvcConfigurer}
 * (see an example in {@code AbstractCrudControllerTest.TestConfig}).
 *
 * @author Sergei Poznanski
 */
@SuppressWarnings("WeakerAccess")
public class CrudPageSerializer extends JsonSerializer<Page> {

	/**
	 * Specifies which alias to use for "content" field in the serialized view.
	 * Default value is {@link ContentAliasMode#FIRST_WORD}
	 */
	protected ContentAliasMode contentAliasMode = ContentAliasMode.FIRST_WORD;

	/**
	 * Specifies the alias for "content" field in case of using the {@link ContentAliasMode#DEFAULT_NAME} mode.
	 */
	protected String defaultContentName = "content";

	protected String pageBlock = "page";
	protected String pageNumber = "number";
	protected String pageSize = "size";
	protected String pageTotal = "total";
	protected String pageFirst = "first";
	protected String pageLast = "last";

	protected String elementsBlock = "elements";
	protected String elementsTotal = "total";
	protected String elementsExposed = "exposed";

	protected String sortBlock = "sort";
	protected String sortedProperty = "property";
	protected String sortedDirection = "direction";

	@Override
	public void serialize(final Page page, final JsonGenerator gen, final SerializerProvider provider) throws IOException {

		List content = page.getContent();

		gen.writeStartObject();
		{
			if (!content.isEmpty()) {
				gen.writeObjectField(getContentAlias(content), content);
			}

			gen.writeObjectFieldStart(pageBlock);
			{
				gen.writeNumberField(pageNumber, page.getNumber());
				gen.writeNumberField(pageSize, page.getSize());
				gen.writeNumberField(pageTotal, page.getTotalPages());
				gen.writeBooleanField(pageFirst, page.isFirst());
				gen.writeBooleanField(pageLast, page.isLast());
			}
			gen.writeEndObject();

			gen.writeObjectFieldStart(elementsBlock);
			{
				gen.writeNumberField(elementsTotal, page.getTotalElements());
				gen.writeNumberField(elementsExposed, page.getNumberOfElements());
			}
			gen.writeEndObject();

			Sort sort = page.getSort();
			if (sort.isSorted()) {
				gen.writeArrayFieldStart(sortBlock);
				{
					for (Sort.Order order : sort) {
						gen.writeStartObject();
						{
							gen.writeStringField(sortedProperty, order.getProperty());
							gen.writeStringField(sortedDirection, order.getDirection().name());
						}
						gen.writeEndObject();
					}
				}
				gen.writeEndArray();
			}
		}
		gen.writeEndObject();
	}

	private String getContentAlias(final List content) {

		String alias = defaultContentName;

		if (!content.isEmpty()) {
			Object obj = content.get(0);
			ContentAlias contentAlias = obj.getClass().getAnnotation(ContentAlias.class);
			if (contentAlias != null) {
				alias = contentAlias.value();
			} else {
				switch (contentAliasMode) {
					case FIRST_WORD:
						alias = English.plural(CrudUtils.firstWordOf(obj.getClass().getSimpleName()));
						break;
					case SNAKE_CASE:
						alias = CrudUtils.toSnakeCase(English.plural(obj.getClass().getSimpleName()));
						break;
					case CAMEL_CASE:
						alias = English.plural(obj.getClass().getSimpleName());
						break;
				}
			}
		}
		return alias;
	}

	/**
	 * Defines the mode of displaying the "content" field.
	 */
	public enum ContentAliasMode {

		/**
		 * The plural form of the first word of the "content" class is used. For example:<br/>
		 * {@code UserResponse -> users}
		 *
		 */
		FIRST_WORD,

		/**
		 * A "snake case" of the "content" class in the plural form is used. For example:<br/>
		 * {@code UserResponse -> user_responses}
		 */
		SNAKE_CASE,

		/**
		 * A "camel case" of the "content" class in the plural form is used. For example:<br/>
		 * {@code UserResponse -> UserResponses}
		 */
		CAMEL_CASE,

		/**
		 * The value of {@link CrudPageSerializer#defaultContentName} is used.
		 */
		DEFAULT_NAME
	}
}
