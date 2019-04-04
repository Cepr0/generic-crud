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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set the <u>custom plural</u> alias of the embedded content in the {@code Page} object,
 * which is processed in the custom {@code Page} serializer (see {@code CrudPageSerializer}).
 * <br/>
 * It can be used to mark any entities or response DTOs for which you need to set the custom plural name
 * in the serialized representation of the {@code Page} object.
 *
 * @author Sergei Poznanski
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentAlias {
	/**
	 * Defines the plural alias of the marked entity.
	 *
	 * @return the plural alias of the entity.
	 */
	String value();
}
