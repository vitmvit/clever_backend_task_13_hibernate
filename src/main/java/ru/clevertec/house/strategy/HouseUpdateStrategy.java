package ru.clevertec.house.strategy;

import ru.clevertec.house.model.entity.House;

/**
 * Интерфейс стратегии обновления объектов типа House.
 * <p>
 * Определяет метод для обновления существующего объекта House.
 */
public interface HouseUpdateStrategy {

    House update(House house);
}