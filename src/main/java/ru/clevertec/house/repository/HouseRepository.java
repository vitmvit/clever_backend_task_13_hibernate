package ru.clevertec.house.repository;

import ru.clevertec.house.model.entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseRepository {

    House getByUuid(UUID uuid);

    List<House> getAll(int limit, int offset);

    List<House> getByCityContaining(String city);

    House create(House house);

    House update(House house);

    void deleteByUuid(UUID uuid);
}
