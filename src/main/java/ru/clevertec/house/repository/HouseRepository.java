package ru.clevertec.house.repository;

import ru.clevertec.house.model.entity.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseRepository {

    Optional<House> getByUUID(UUID uuid);

    List<House> getAll();

    House create(House house);

    House update(House house);

    void deleteByUUID(UUID uuid);
}
