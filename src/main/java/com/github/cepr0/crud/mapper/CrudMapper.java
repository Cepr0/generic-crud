package com.github.cepr0.crud.mapper;

import com.github.cepr0.crud.dto.CrudRequest;
import com.github.cepr0.crud.dto.CrudResponse;
import com.github.cepr0.crud.model.IdentifiableEntity;
import org.mapstruct.MappingTarget;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public interface CrudMapper<T extends IdentifiableEntity<ID>, ID extends Serializable, Q extends CrudRequest, S extends CrudResponse<ID>> {
	@NonNull T toCreate(@NonNull Q request);
	@NonNull T toUpdate(@MappingTarget @NonNull T entity, @NonNull Q request);
	@NonNull S toResponse(@NonNull T entity);
}
