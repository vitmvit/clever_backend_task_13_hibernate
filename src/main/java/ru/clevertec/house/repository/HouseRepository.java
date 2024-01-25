package ru.clevertec.house.repository;

import ru.clevertec.house.model.entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseRepository {

    House getByUUID(UUID uuid);

    List<House> getAll(int limit, int offset);

    List<House> getBySurnameContaining(String city);

    House create(House house);

    House update(House house);

    void deleteByUUID(UUID uuid);


}
