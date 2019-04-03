package io.github.cepr0.demo.user;

import io.github.cepr0.crud.mapper.CrudMapper;
import io.github.cepr0.crud.mapper.ReferenceMapper;
import io.github.cepr0.demo.model.User;
import io.github.cepr0.demo.user.dto.UserRequest;
import io.github.cepr0.demo.user.dto.UserResponse;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
		nullValueCheckStrategy = ALWAYS,
		nullValueMappingStrategy = RETURN_DEFAULT,
		nullValuePropertyMappingStrategy = IGNORE
)
public abstract class UserMapper implements CrudMapper<User, UserRequest, UserResponse>, ReferenceMapper<User, Long> {
	@Autowired @Getter private UserRepo repo;
}
