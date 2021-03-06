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

package io.github.cepr0.demo.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.dto.CrudRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author Sergei Poznanski
 */
@Data
public class CarRequest implements CrudRequest {

	@JsonProperty("personId")
	@NotNull(groups = OnCreate.class)
	private UUID person;

	@NotBlank(groups = OnCreate.class)
	private String brand;

	@NotBlank(groups = OnCreate.class)
	private String model;

	@NotNull(groups = OnCreate.class)
	@Min(1900)
	private Integer year;
}
