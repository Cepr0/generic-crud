package io.github.cepr0.crud.event;

import io.github.cepr0.crud.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Integer> {
}
