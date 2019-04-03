package io.github.cepr0.crud.model;

import java.io.Serializable;

public abstract class JpaEntity<ID extends Serializable> implements IdentifiableEntity<ID> {
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{id=" + getId() + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return getId() != null && getId().equals(((JpaEntity) o).getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
