package io.github.cepr0.demo.api;

import com.fasterxml.jackson.annotation.JsonView;
import io.github.cepr0.crud.api.AbstractCrudController;
import io.github.cepr0.crud.api.OnCreate;
import io.github.cepr0.crud.api.OnUpdate;
import io.github.cepr0.demo.model.Car;
import io.github.cepr0.demo.person.CarService;
import io.github.cepr0.demo.person.dto.CarRequest;
import io.github.cepr0.demo.person.dto.CarResponse;
import io.github.cepr0.demo.person.dto.Views;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cars")
public class CarController extends AbstractCrudController<Car, UUID, CarRequest, CarResponse> {

	public CarController(@NonNull final CarService service) {
		super(service);
	}

	@JsonView(Views.ForCar.class)
	@PostMapping
	@Override
	public CarResponse create(@Validated(OnCreate.class) @RequestBody @NonNull final CarRequest request) {
		return super.create(request);
	}

	@JsonView(Views.ForCar.class)
	@PatchMapping("/{id}")
	@Override
	public ResponseEntity<CarResponse> update(@PathVariable("id") @NonNull final UUID id, @Validated(OnUpdate.class) @RequestBody @NonNull final CarRequest request) {
		return super.update(id, request);
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity delete(@PathVariable("id") @NonNull final UUID id) {
		return super.delete(id);
	}

	@JsonView(Views.ForCar.class)
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<CarResponse> getOne(@PathVariable("id") @NonNull final UUID id) {
		return super.getOne(id);
	}

	@JsonView(Views.ForCar.class)
	@GetMapping
	@Override
	public List<CarResponse> getAll() {
		return super.getAll();
	}
}
