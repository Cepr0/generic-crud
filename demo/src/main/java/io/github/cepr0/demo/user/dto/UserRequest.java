package io.github.cepr0.demo.user.dto;

import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.dto.CrudRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequest implements CrudRequest {
	@NotBlank(groups = OnCreate.class)
	private String name;
}
