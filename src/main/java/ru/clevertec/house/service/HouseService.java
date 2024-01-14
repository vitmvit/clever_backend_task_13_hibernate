package ru.clevertec.house.service;

import ru.clevertec.house.model.entity.House;

import java.util.List;
import java.util.UUID;

public interface HouseService {

    House getByUUID(UUID uuid);

    List<House> getAll();

    House create(House dto);

    House update(House dto);

    void delete(UUID uuid);
}
