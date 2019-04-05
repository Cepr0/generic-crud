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

package io.github.cepr0.crud.mapper;

import org.springframework.lang.NonNull;

/**
 * Functional interface used to provide mapping between to arbitrary beans.
 *
 * @param <S> type of the source bean
 * @param <T> type of the target bean
 *
 * @author Serhei Poznanski
 */
@FunctionalInterface
public interface BeanMapper<S, T> {

	/**
	 * Maps the source bean to the target bean.
	 *
	 * @param source must not be {@code null}
	 * @param target must not be {@code null}
	 * @return the target bean
	 */
	@NonNull T map(@NonNull S source, @NonNull T target);
}
