package io.github.cepr0.demo.person.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.github.cepr0.crud.dto.CrudResponse;
import io.github.cepr0.crud.model.ContentAlias;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@ContentAlias("people")
@Data
public class PersonResponse implements CrudResponse<UUID> {
	@JsonView(Views.ForPerson.class) private UUID id;
	@JsonView(Views.ForPerson.class) private String name;
	@JsonView(Views.ForPerson.class) private Set<CarResponse> cars;
}
