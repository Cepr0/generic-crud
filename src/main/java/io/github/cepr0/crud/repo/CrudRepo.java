package io.github.cepr0.crud.repo;

import io.github.cepr0.crud.model.IdentifiableEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CrudRepo<T extends IdentifiableEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

	@NonNull Optional<T> getById(@NonNull ID id);

	@NonNull Optional<T> getToUpdateById(@NonNull ID id);

	@Query("select e from #{#entityName} e")
	@NonNull
	List<T> getAll(@NonNull Pageable pageable);

	@Query("select e from #{#entityName} e")
	@NonNull
	List<T> getAll(@NonNull Sort sort);

	@Query("select e from #{#entityName} e")
	@NonNull
	List<T> getAll();

	@NonNull
	@Modifying
	@Query("delete from #{#entityName} e where e.id = ?1")
	int delById(@NonNull ID id);
}
