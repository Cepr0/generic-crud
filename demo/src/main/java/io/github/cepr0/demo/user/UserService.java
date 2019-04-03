package io.github.cepr0.demo.user;

import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.demo.model.User;
import io.github.cepr0.demo.user.dto.UserRequest;
import io.github.cepr0.demo.user.dto.UserResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractCrudService<User, Long, UserRequest, UserResponse> {
	public UserService(@NonNull final UserRepo repo, @NonNull final UserMapper mapper) {
		super(repo, mapper);
	}
}
