package ru.clevertec.house.service;

import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    Person getByUuid(UUID uuid);

    List<Person> getAll(int offset, int limit);

    List<Person> searchBySurname(String surname);

    Person create(Person person);

    Person update(Person person);

    Person patch(Person person);

    void delete(UUID uuid);

    List<House> getAllHouses(UUID uuid);
}
