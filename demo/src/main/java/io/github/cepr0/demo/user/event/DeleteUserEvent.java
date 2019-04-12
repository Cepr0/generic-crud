package io.github.cepr0.demo.user.event;

import io.github.cepr0.crud.event.EntityEvent;
import io.github.cepr0.demo.model.User;

/**
 * @author Sergei Poznanski
 */
public class DeleteUserEvent extends EntityEvent<User> {
	public DeleteUserEvent(final User entity) {
		super(entity);
	}
}
