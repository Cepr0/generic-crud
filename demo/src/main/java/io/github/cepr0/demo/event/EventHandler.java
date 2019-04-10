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

package io.github.cepr0.demo.event;

import io.github.cepr0.crud.event.CreateEntityEvent;
import io.github.cepr0.crud.event.DeleteEntityEvent;
import io.github.cepr0.crud.event.EntityEvent;
import io.github.cepr0.crud.event.UpdateEntityEvent;
import io.github.cepr0.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

/**
 * Example of using the entity events ({@link EntityEvent}).
 * With {@link EventListener} and/or {@link TransactionalEventListener} you can handle them
 * either within a transaction or after.
 * <p>
 * With help of {@link EntityEvent#contain} method you can filter the events of entities by their simple class name
 * (see parameter 'condition' of the event listener).
 *
 * @author Sergei Poznanski
 */
@Slf4j
@Component
public class EventHandler {

	@EventListener(condition = "#event.contain('User')")
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleCreateUserEvent(CreateEntityEvent event) {
		User user = (User) event.getEntity();
		user.setCreatedAt(Instant.now());
		log.info("[i] Log creating a User within a transaction: {}", user);
	}

	@EventListener(condition = "!#event.contain('User')")
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleCreateNonUserEvent(CreateEntityEvent event) {
		log.info("[i] Log creating an entity within a transaction: {}", event.getEntity());
	}

	@Async
	@TransactionalEventListener
	public void handleCreateEntityEventAfterCommit(CreateEntityEvent event) {
		log.info("[i] Log creating an entity after commit: {}", event.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleUpdateEntityEvent(UpdateEntityEvent event) {
		log.info("[i] Log updating an entity within a transaction: {}", event.getEntity());
	}

	@Async
	@TransactionalEventListener
	public void handleUpdateEntityEventAfterCommit(UpdateEntityEvent event) {
		log.info("[i] Log updating an entity after commit: {}", event.getEntity());
	}

	@EventListener(condition = "#event.contain('User')")
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleDeleteUserEvent(DeleteEntityEvent event) {
		log.info("[i] Log deleting a User within a transaction: {}", event.getEntity());
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Deleting a User is not allowed!");
	}

	@EventListener(condition = "!#event.contain('User')")
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleDeleteNonUserEvent(DeleteEntityEvent event) {
		log.info("[i] Log deleting an entity within a transaction: {}", event.getEntity());
	}

	@Async
	@TransactionalEventListener
	public void handleDeleteEntityEventAfterCommit(DeleteEntityEvent event) {
		log.info("[i] Log deleting an entity after commit: {}", event.getEntity());
	}
}
