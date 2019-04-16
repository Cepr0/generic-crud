/*
 * Copyright 2019 Generic-CRUD contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cepr0.demo.user;

import io.github.cepr0.crud.repo.JpaRepo;
import io.github.cepr0.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Sergei Poznanski
 */
public interface UserRepo extends JpaRepo<User, Long> {

	@Override
	default void delete(User user) {
		user.setDeleted(true);
	}

	@Query("select u from User u where u.id = ?1 and u.deleted = false")
	@Override
	Optional<User> getToDeleteById(Long aLong);

	@Query("select u from User u where u.id = ?1 and u.deleted = false")
	@Override
	Optional<User> getById(Long aLong);

	@Query("select u from User u where u.deleted = false")
	@Override
	Page<User> getAll(Pageable pageable);
}
