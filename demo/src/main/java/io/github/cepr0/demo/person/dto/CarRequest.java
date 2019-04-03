package io.github.cepr0.demo.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.dto.CrudRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
