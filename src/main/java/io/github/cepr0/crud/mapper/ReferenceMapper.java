package io.github.cepr0.crud.mapper;

import io.github.cepr0.crud.model.IdentifiableEntity;
import io.github.cepr0.crud.repo.CrudRepo;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public interface ReferenceMapper<T extends IdentifiableEntity<ID>, ID extends Serializable> {

	@NonNull CrudRepo<T, ID> getRepo();

	@NonNull
	default T toReference(@NonNull ID id) {
		return getRepo().getOne(id);
	}

	@Nullable
	default ID toId(@NonNull T entity) {
		return entity.getId();
	}

	@NonNull
	default Set<T> toRefSet(@NonNull Collection<ID> ids) {
		return ids.stream().map(this::toReference).collect(toSet());
	}

	@NonNull
	default Set<ID> toIdSet(@NonNull Collection<T> entities) {
		return entities.stream().map(this::toId).filter(Objects::nonNull).collect(toSet());
	}

	@NonNull
	default List<T> toRefList(@NonNull Collection<ID> ids) {
		return ids.stream().map(this::toReference).collect(toList());
	}

	@NonNull
	default List<ID> toIdList(@NonNull Collection<T> entities) {
		return entities.stream().map(this::toId).filter(Objects::nonNull).collect(toList());
	}
}
