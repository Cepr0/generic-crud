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

package io.github.cepr0.test.handler;

import io.github.cepr0.test.event.CreateModelEvent;
import io.github.cepr0.test.event.DeleteModelEvent;
import io.github.cepr0.test.event.UpdateModelEvent;
import io.github.cepr0.test.model.Event;
import io.github.cepr0.test.model.Model;
import io.github.cepr0.test.repo.EventRepo;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Serhei Poznanski
 */
@Component
public class ModelEventHandler {

	private final EventRepo eventRepo;

	public ModelEventHandler(@NonNull final EventRepo eventRepo) {
		this.eventRepo = eventRepo;
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleCreateModelEvent(@NonNull final CreateModelEvent e) {
		processEvent(Event.Type.CREATE, e.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleUpdateModelEvent(@NonNull final UpdateModelEvent e) {
		processEvent(Event.Type.UPDATE, e.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleDeleteModelEvent(@NonNull final DeleteModelEvent e) {
	processEvent(Event.Type.DELETE, e.getEntity());
	}

	private void processEvent(@NonNull final Event.Type type, @NonNull final Model model) {
		Event event = new Event(type, model);
		eventRepo.save(event);
	}
}
