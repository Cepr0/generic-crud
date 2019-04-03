package io.github.cepr0.crud.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

public abstract class CrudUtils {

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

	@NonNull
	public static String firstWordOf(@NonNull final String camelCaseString) {
		return splitCamelCase(camelCaseString)[0].toLowerCase();
	}

	@NonNull
	public static String toSnakeCase(@NonNull final String camelCaseString) {
		return Arrays.stream(splitCamelCase(camelCaseString))
				.map(String::toLowerCase)
				.collect(Collectors.joining("_"));
	}

	@NonNull
	public static String[] splitCamelCase(@NonNull final String camelCaseString) {
		// https://stackoverflow.com/a/7594052
		return camelCaseString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
	}
}
