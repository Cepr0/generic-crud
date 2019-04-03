package io.github.cepr0.demo.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.github.cepr0.crud.dto.CrudResponse;
import lombok.Data;

import java.util.UUID;

@Data
public class CarResponse implements CrudResponse<UUID> {
	@JsonView(Views.ForPerson.class) private UUID id;
	@JsonView(Views.ForCar.class) @JsonProperty("personId") private UUID person;
	@JsonView(Views.ForPerson.class) private String brand;
	@JsonView(Views.ForPerson.class) private String model;
	@JsonView(Views.ForPerson.class) private Integer year;
}
