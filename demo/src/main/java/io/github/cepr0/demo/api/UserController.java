package io.github.cepr0.demo.api;

import io.github.cepr0.crud.api.AbstractCrudController;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.api.OnUpdate;
import io.github.cepr0.demo.model.User;
import io.github.cepr0.demo.user.UserService;
import io.github.cepr0.demo.user.dto.UserRequest;
import io.github.cepr0.demo.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController extends AbstractCrudController<User, Long, UserRequest, UserResponse> {

	public UserController(@NonNull final UserService service) {
		super(service);
	}

	@PostMapping
	@Override
	public UserResponse create(@Validated(OnCreate.class) @RequestBody @NonNull final UserRequest request) {
		return super.create(request);
	}

	@PatchMapping("/{id}")
	@Override
	public ResponseEntity<UserResponse> update(@PathVariable("id") @NonNull final Long id, @Validated(OnUpdate.class) @RequestBody @NonNull final UserRequest request) {
		return super.update(id, request);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity delete(@PathVariable("id") @NonNull final Long id) {
		return super.delete(id);
	}

	@GetMapping("/{id}")
	@Override
	public ResponseEntity<UserResponse> getOne(@PathVariable("id") @NonNull final Long id) {
		return super.getOne(id);
	}

	@GetMapping
	@Override
	public Page<UserResponse> getAll(Pageable pageable) {
		return super.getAll(pageable);
	}
}
