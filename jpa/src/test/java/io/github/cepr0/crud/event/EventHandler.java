package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.Event;
import io.github.cepr0.crud.model.IdentifiableEntity;
import io.github.cepr0.crud.model.Model;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventHandler {

	private final EventRepo eventRepo;

	public EventHandler(@NonNull final EventRepo eventRepo) {
		this.eventRepo = eventRepo;
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleCreateEntityEvent(@NonNull final CreateEntityEvent e) {
		processEvent(Event.Type.CREATE, e.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleUpdateEntityEvent(@NonNull final UpdateEntityEvent e) {
		processEvent(Event.Type.UPDATE, e.getEntity());
	}

	@EventListener
	@Transactional(propagation = Propagation.MANDATORY)
	public void handleDeleteEntityEvent(@NonNull final DeleteEntityEvent e) {
	processEvent(Event.Type.DELETE, e.getEntity());
	}

	private void processEvent(@NonNull final Event.Type type, @NonNull final IdentifiableEntity entity) {
		Event event = new Event(type, (Model) entity);
		eventRepo.save(event);
	}
}
