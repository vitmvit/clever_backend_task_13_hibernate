package ru.clevertec.house.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.house.model.entity.Person;

import java.util.List;
import java.util.UUID;

public class PersonListener {

    /**
     * Вызывается перед сохранением новой сущности Person в базе данных и устанавливает uuid для нее.
     *
     * @param person Сущность Person, которая будет сохранена.
     */
    @PrePersist
    public void persist(Person person) {
        person.setUuid(UUID.randomUUID());
        person.setHouses(List.of());
    }
}
