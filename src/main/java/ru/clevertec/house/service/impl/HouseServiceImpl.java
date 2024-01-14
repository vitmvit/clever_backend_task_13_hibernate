package ru.clevertec.house.service.impl;

import lombok.AllArgsConstructor;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.ResourceNotFoundException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.HouseService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository repository;

    @Override
    public House getByUUID(UUID uuid) {
        return repository.getByUUID(uuid).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<House> getAll() {
        return repository.getAll();
    }

    @Override
    public House create(House house) {
        try {
            return repository.create(house);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e);
        }
    }

    @Override
    public House update(House house) {
        return repository.update(house);
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteByUUID(uuid);
    }
}

