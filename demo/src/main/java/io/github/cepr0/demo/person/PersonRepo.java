package io.github.cepr0.demo.person;

import io.github.cepr0.crud.repo.JpaRepo;
import io.github.cepr0.demo.model.Person;

import java.util.UUID;

public interface PersonRepo extends JpaRepo<Person, UUID> {
}
