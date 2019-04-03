package io.github.cepr0.demo.user.dto;

import io.github.cepr0.crud.dto.CrudResponse;
import lombok.Data;

@Data
public class UserResponse implements CrudResponse<Long> {
	private Long id;
	private String name;
}
