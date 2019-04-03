package io.github.cepr0.crud.model;

import java.io.Serializable;

public interface IdentifiableEntity<ID extends Serializable> extends Serializable {
	ID getId();
}
