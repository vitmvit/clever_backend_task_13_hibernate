package ru.clevertec.house.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.house.model.entity.House;

import java.util.UUID;

public class HouseListener {

    /**
     * Вызывается перед сохранением новой сущности House в базе данных и устанавливает uuid для нее.
     *
     * @param house Сущность House, которая будет сохранена.
     */
    @PrePersist
    public void persist(House house) {
        house.setUuid(UUID.randomUUID());
    }
}
