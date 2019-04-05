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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * A set of utility methods used in the library.
 *
 * @author Sergei Poznanski
 */
public abstract class CrudUtils {

	/**
	 * A variant of the {@link BeanUtils#copyProperties},
	 * which copy only non null properties of the source bean to the target bean.
	 *
	 * @param source the source bean, must not be {@code null}
	 * @param target the target bean, must not be {@code null}
	 * @param ignoredProperties array of property names to ignore
	 * @param <S> type of the source bean
	 * @param <T> type of the target bean
	 * @return target bean, will never be {@code null}
	 * @throws BeansException if the copying failed
	 */
	@NonNull
	public static <S, T> T copyNonNullProperties(@NonNull final S source, @NonNull final T target, String... ignoredProperties) throws BeansException {

		Class<?> targetClass = target.getClass();
		PropertyDescriptor[] targetProperties = getPropertyDescriptors(targetClass);
		List<String> ignoreList = (ignoredProperties != null ? Arrays.asList(ignoredProperties) : null);

		for (PropertyDescriptor targetProperty : targetProperties) {

			Method targetPropSetter = targetProperty.getWriteMethod();
			String targetPropName = targetProperty.getName();

			if (targetPropSetter != null && (ignoreList == null || !ignoreList.contains(targetPropName))) {

				PropertyDescriptor sourceProperty = getPropertyDescriptor(source.getClass(), targetPropName);

				if (sourceProperty != null) {

					Method sourcePropGetter = sourceProperty.getReadMethod();

					if (sourcePropGetter != null && ClassUtils.isAssignable(targetPropSetter.getParameterTypes()[0], sourcePropGetter.getReturnType())) {
						try {
							if (!Modifier.isPublic(sourcePropGetter.getDeclaringClass().getModifiers())) {
								sourcePropGetter.setAccessible(true);
							}
							Object value = sourcePropGetter.invoke(source);
							if (value != null) {
								if (!Modifier.isPublic(targetPropSetter.getDeclaringClass().getModifiers())) {
									targetPropSetter.setAccessible(true);
								}
								targetPropSetter.invoke(target, value);
							}
						} catch (Throwable ex) {
							throw new FatalBeanException("Could not copy property '" + targetPropName + "' from source to target", ex);
						}
					}
				}
			}
		}
		return target;
	}

	/**
	 * Takes the first word of the given 'CamelCase' string.
	 *
	 * @param camelCaseString given 'CamelCase' string, must not be {@code null}
	 * @return return the first word of the given string
	 */
	@NonNull
	public static String firstWordOf(@NonNull final String camelCaseString) {
		return splitCamelCase(camelCaseString)[0].toLowerCase();
	}

	/**
	 * Converts a given 'CamelCase' string to the 'snake_case' variant.
	 *
	 * @param camelCaseString given 'CamelCase' string, must not be {@code null}
	 * @return the 'snake_case' string
	 */
	@NonNull
	public static String toSnakeCase(@NonNull final String camelCaseString) {
		return Arrays.stream(splitCamelCase(camelCaseString))
				.map(String::toLowerCase)
				.collect(Collectors.joining("_"));
	}

	/**
	 * Splits a given 'CamelCase' string to the array of its lower-cased words.
	 *
	 * @param camelCaseString given 'CamelCase' string, must not be {@code null}
	 * @return the array of its lower-cased words
	 */
	@NonNull
	public static String[] splitCamelCase(@NonNull final String camelCaseString) {
		Objects.requireNonNull(camelCaseString, "The parameter 'camelCaseString' must not be null!");
		// https://stackoverflow.com/a/7594052
		return camelCaseString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
	}
}
