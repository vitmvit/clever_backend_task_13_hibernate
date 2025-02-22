package ru.clevertec.house.service;

import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    House getByUuid(UUID uuid);

    List<House> getAll(int offset, int limit);

    List<House> searchByCity(String city);

    House create(House house);

    House update(House house);

    House patch(House house);

    void delete(UUID uuid);

    List<Person> getAllResidents(UUID uuid);
}