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

@SuppressWarnings("WeakerAccess")
public class CrudPageSerializer extends JsonSerializer<Page> {

	protected ContentAliasMode contentAliasMode = ContentAliasMode.FIRST_WORD;

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

	public enum ContentAliasMode {
		FIRST_WORD, SNAKE_CASE, CAMEL_CASE, DEFAULT_NAME
	}
}
