package io.github.cepr0.crud.mapper;

import org.springframework.lang.NonNull;

@FunctionalInterface
public interface BeanMapper<S, T> {
	@NonNull T map(@NonNull S source, @NonNull T target);
}
