package io.github.cepr0.crud.repo;

import io.github.cepr0.crud.model.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ModelRepo extends JpaRepo<Model, Integer> {
	@NonNull
	@Override
	Optional<Model> getToUpdateById(@NonNull Integer integer);

	@NonNull
	@Override
	Optional<Model> getToDeleteById(@NonNull Integer integer);

	@NonNull
	@Override
	Optional<Model> getById(@NonNull Integer integer);

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<Model> getAll();

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	Page<Model> getAll(@NonNull Pageable pageable);

	@Query("select e from #{#entityName} e")
	@NonNull
	@Override
	List<Model> getAll(@NonNull Sort sort);
}
