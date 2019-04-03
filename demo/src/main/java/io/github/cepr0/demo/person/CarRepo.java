package io.github.cepr0.demo.person;

import io.github.cepr0.crud.repo.JpaRepo;
import io.github.cepr0.demo.model.Car;

import java.util.UUID;

public interface CarRepo extends JpaRepo<Car, UUID> {
}
