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

import io.github.cepr0.crud.dto.CrudRequest;
import io.github.cepr0.crud.dto.CrudResponse;
import io.github.cepr0.crud.model.IdentifiableEntity;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.lang.NonNull;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * Mapper interface that used to map entities and their input (request) and output (response) DTOs.
 * <br/><br/>
 * This interface relays on <a href = "http://mapstruct.org/">MapStruct</a> framework.
 * The inheritors of this interface must relay on that framework as well and
 * use the mapper configuration defined on this interface (i.e. {@code @Mapper(config = CrudMapper.class)}).
 * This configuration defines the following:
 * <ul>
 * 	<li>the target bean type always instantiated and returned regardless of whether the source is NULL or not</li>
 *    <li>the source property values are always checked for null</li>
 *    <li>if a source bean property equals null the target bean property will be ignored and retain its existing value</li>
 * </ul>
 *
 * @param <T> type of the entity which extends {@link IdentifiableEntity}
 * @param <Q> type of the input (request) DTO
 * @param <S> type of the output (response) DTO
 *
 * @author Serhei Poznanski
 */
@MapperConfig(
		nullValueMappingStrategy = RETURN_DEFAULT,
		nullValueCheckStrategy = ALWAYS,
		nullValuePropertyMappingStrategy = IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		componentModel = "spring"
)
public interface CrudMapper<T extends IdentifiableEntity, Q extends CrudRequest, S extends CrudResponse> {

	/**
	 * Maps an input (request) DTO to the related entity to be created.
	 *
	 * @param request input (request) DTO, must not be {@code null}
	 * @return related entity, never be {@code null}
	 */
	@NonNull T toCreate(@NonNull Q request);

	/**
	 * Maps an input (request) DTO to the related entity to be updated.
	 *
	 * @param request input (request) DTO, must not be {@code null}
	 * @param target updated entity, must not be {@code null}
	 * @return updated entity, never be {@code null}
	 */
	@NonNull T toUpdate(@NonNull Q request, @MappingTarget @NonNull T target);

	/**
	 * Maps an entity to the related output (response) DTO.
	 *
	 * @param entity must not be {@code null}
	 * @return output (response) DTO, never be {@code null}
	 */
	@NonNull S toResponse(@NonNull T entity);
}
