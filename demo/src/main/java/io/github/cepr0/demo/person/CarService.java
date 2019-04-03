package io.github.cepr0.demo.person;

import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.demo.model.Car;
import io.github.cepr0.demo.person.dto.CarRequest;
import io.github.cepr0.demo.person.dto.CarResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarService extends AbstractCrudService<Car, UUID, CarRequest, CarResponse> {
	public CarService(final CarRepo repo, final CarMapper mapper) {
		super(repo, mapper);
	}
}
