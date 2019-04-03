package io.github.cepr0.demo.person;

import io.github.cepr0.crud.mapper.CrudMapper;
import io.github.cepr0.crud.mapper.ReferenceMapper;
import io.github.cepr0.demo.model.Person;
import io.github.cepr0.demo.person.dto.PersonRequest;
import io.github.cepr0.demo.person.dto.PersonResponse;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		nullValueCheckStrategy = ALWAYS,
		nullValueMappingStrategy = RETURN_DEFAULT,
		nullValuePropertyMappingStrategy = IGNORE,
		uses = CarMapper.class
)
public abstract class PersonMapper implements CrudMapper<Person, PersonRequest, PersonResponse>, ReferenceMapper<Person, UUID> {
	@Autowired @Getter private PersonRepo repo;
}
