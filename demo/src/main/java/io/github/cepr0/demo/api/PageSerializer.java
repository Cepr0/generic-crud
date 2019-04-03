package io.github.cepr0.demo.api;

import io.github.cepr0.crud.api.CrudPageSerializer;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class PageSerializer extends CrudPageSerializer {
	public PageSerializer() {
		elementsOnPage = "on_page";
		contentAliasMode = ContentAliasMode.FIRST_WORD;
	}
}
