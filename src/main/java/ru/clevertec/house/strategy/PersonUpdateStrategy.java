package ru.clevertec.house.strategy;

import ru.clevertec.house.model.entity.Person;

/**
 * Интерфейс стратегии обновления объектов типа Person.
 * <p>
 * Определяет метод для обновления существующего объекта Person.
 */
public interface PersonUpdateStrategy {

    Person update(Person person);
}