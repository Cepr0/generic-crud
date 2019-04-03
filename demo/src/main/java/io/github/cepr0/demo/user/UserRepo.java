package io.github.cepr0.demo.user;

import io.github.cepr0.crud.repo.JpaRepo;
import io.github.cepr0.demo.model.User;

public interface UserRepo extends JpaRepo<User, Long> {
}
