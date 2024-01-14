package ru.clevertec.house.service.impl;

import lombok.AllArgsConstructor;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.ResourceNotFoundException;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Override
    public Person getByUUID(UUID uuid) {
        return repository.getByUUID(uuid).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Person> getAll() {
        return repository.getAll();
    }

    @Override
    public Person create(Person person) {
        try {
            return repository.create(person);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e);
        }
    }

    @Override
    public Person update(Person person) {
        return repository.update(person);
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteByUUID(uuid);
    }
}