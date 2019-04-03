package io.github.cepr0.crud.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ModelResponse implements CrudResponse<Integer> {
	private Integer id;
	private String text;
	private Integer number;
}
