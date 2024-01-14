package ru.clevertec.house.service;

import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    Person getByUUID(UUID uuid);

    List<Person> getAll();

    Person create(Person dto);

    Person update(Person dto);

    void delete(UUID uuid);
}
