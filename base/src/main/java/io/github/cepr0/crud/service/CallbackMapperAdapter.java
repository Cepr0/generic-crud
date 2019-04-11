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

import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author Sergei Poznanski
 */
class CallbackMapperAdapter<S, T> implements BiFunction<S, T, T> {

	private final BiFunction<S, T, T> mapper;
	private final BiConsumer<S, T> callback;

	CallbackMapperAdapter(@NonNull final BiFunction<S, T, T> mapper, @NonNull final BiConsumer<S, T> callback) {
		this.mapper = Objects.requireNonNull(mapper, "Parameter 'mapper' must not be null!");
		this.callback = Objects.requireNonNull(callback, "Parameter 'callback' must not be null!");
	}

	@NonNull
	@Override
	public T apply(@NonNull final S source, @NonNull final T target) {
		T result = mapper.apply(source, target);
		callback.accept(source, result);
		return result;
	}
}
