package io.github.cepr0.demo.person;

import io.github.cepr0.crud.service.AbstractCrudService;
import io.github.cepr0.demo.model.Person;
import io.github.cepr0.demo.person.dto.PersonRequest;
import io.github.cepr0.demo.person.dto.PersonResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService extends AbstractCrudService<Person, UUID, PersonRequest, PersonResponse> {
	public PersonService(final PersonRepo repo, final PersonMapper mapper) {
		super(repo, mapper);
	}
}
