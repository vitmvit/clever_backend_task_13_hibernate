package ru.clevertec.house.repository;

import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {

    Optional<Person> getByUUID(UUID uuid);

    List<Person> getAll();

    Person create(Person person);

    Person update(Person person);

    void deleteByUUID(UUID uuid);
}
