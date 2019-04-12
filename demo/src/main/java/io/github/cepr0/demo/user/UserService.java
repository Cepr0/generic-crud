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

import io.github.cepr0.crud.event.EntityEvent;
import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.demo.model.User;
import io.github.cepr0.demo.user.dto.UserRequest;
import io.github.cepr0.demo.user.dto.UserResponse;
import io.github.cepr0.demo.user.event.CreateUserEvent;
import io.github.cepr0.demo.user.event.DeleteUserEvent;
import io.github.cepr0.demo.user.event.UpdateUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

/**
 * Service with example of CRUD operations pre-processing, with callback methods {@code onCreate} and {@code onUpdate},
 * and post-processing with event listeners methods.
 *
 * @author Sergei Poznanski
 */
@Slf4j
@Service
public class UserService extends AbstractCrudService<User, Long, UserRequest, UserResponse> {
	public UserService(@NonNull final UserRepo repo, @NonNull final UserMapper mapper) {
		super(repo, mapper);
	}

	@Override
	protected void onCreate(final UserRequest request, final User user) {
		log.info("[i] Callback onCreate method for {}. Request is: {}", user, request);
		user.setCreatedAt(Instant.now());
		user.setUpdatedAt(Instant.now());
	}

	@Override
	protected void onUpdate(final UserRequest request, final User user) {
		log.info("[i] Callback onUpdate method for {}. Request is: {}", user, request);
		user.setUpdatedAt(Instant.now());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleCreateUserEvent(CreateUserEvent event) {
		log.info("[i] Handling CreateUserEvent of the {} within a transaction", event.getEntity());
	}

	@TransactionalEventListener
	public void handleCreateUserEventAfterCommit(CreateUserEvent event) {
		log.info("[i] Handling CreateUserEvent of the {} after commit", event.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleUpdateUserEvent(UpdateUserEvent event) {
		log.info("[i] Handling UpdateUserEvent of the {} within a transaction", event.getEntity());
	}

	@TransactionalEventListener
	public void handleUpdateUserEventAfterCommit(UpdateUserEvent event) {
		log.info("[i] Handling UpdateUserEvent of the {} after commit", event.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleDeleteUserEvent(DeleteUserEvent event) {
		User user = event.getEntity();
		log.info("[i] Handling DeleteUserEvent of the {} within a transaction", user);
		if (user.getId() == 1) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deleting a User with ID = 1 is not allowed!");
		}
	}

	@TransactionalEventListener
	public void handleDeleteUserEventAfterCommit(DeleteUserEvent event) {
		log.info("[i] Handling DeleteUserEvent of the {} after commit", event.getEntity());
	}

	@Override
	protected EntityEvent<User> onCreateEvent(final User entity) {
		return new CreateUserEvent(entity);
	}

	@Override
	protected EntityEvent<User> onUpdateEvent(final User entity) {
		return new UpdateUserEvent(entity);
	}

	@Override
	protected EntityEvent<User> onDeleteEvent(final User entity) {
		return new DeleteUserEvent(entity);
	}
}
