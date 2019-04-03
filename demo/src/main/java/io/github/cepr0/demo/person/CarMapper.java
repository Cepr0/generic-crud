package io.github.cepr0.demo.person;

import io.github.cepr0.crud.mapper.CrudMapper;
import io.github.cepr0.crud.mapper.ReferenceMapper;
import io.github.cepr0.demo.model.Car;
import io.github.cepr0.demo.person.dto.CarRequest;
import io.github.cepr0.demo.person.dto.CarResponse;
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
		uses = PersonMapper.class
)
public abstract class CarMapper implements CrudMapper<Car, CarRequest, CarResponse>, ReferenceMapper<Car, UUID> {
	@Autowired @Getter CarRepo repo;
}
