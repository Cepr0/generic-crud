package io.github.cepr0.crud.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ModelRequest implements CrudRequest {
	private String text;
	private Integer number;
}
