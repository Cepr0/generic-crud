package io.github.cepr0.crud.mapper;

import io.github.cepr0.crud.dto.ModelRequest;
import io.github.cepr0.crud.dto.ModelResponse;
import io.github.cepr0.crud.model.Model;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		nullValueCheckStrategy = ALWAYS,
		nullValueMappingStrategy = RETURN_DEFAULT,
		nullValuePropertyMappingStrategy = IGNORE
)
public abstract class ModelMapper implements CrudMapper<Model, ModelRequest, ModelResponse> {
}
