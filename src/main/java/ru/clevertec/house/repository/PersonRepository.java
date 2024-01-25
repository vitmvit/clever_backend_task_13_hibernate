package ru.clevertec.house.repository;

import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonRepository {

    Person getByUUID(UUID uuid);

    List<Person> getAll(int limit, int offset);

    List<Person> getByCityContaining(String surname);

    Person create(Person person);

    Person update(Person person);

    void deleteByUUID(UUID uuid);
}
