package io.github.cepr0.demo.user.event;

import io.github.cepr0.crud.event.EntityEvent;
import io.github.cepr0.demo.model.User;

/**
 * @author Sergei Poznanski
 */
public class UpdateUserEvent extends EntityEvent<User> {
	public UpdateUserEvent(final User entity) {
		super(entity);
	}
}
