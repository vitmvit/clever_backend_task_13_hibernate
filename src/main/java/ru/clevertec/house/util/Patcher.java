package ru.clevertec.house.util;

import org.springframework.stereotype.Component;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;

import java.lang.reflect.Field;

/**
 * Класс Patcher предоставляет методы для применения PATCH-запроса на сущности Person и House.
 * Методы используют рефлексию для доступа к приватным полям объектов и установки новых значений.
 */
@Component
public class Patcher {

    /**
     * PATCH-запрос для Person.
     * Копирует значения непустых полей из incompletePerson в existingPerson.
     *
     * @param existingPerson   существующая сущность Person
     * @param incompletePerson сущность Person с обновленными значениями
     * @throws IllegalAccessException если возникла ошибка при доступе к полю
     */
    public void personPatcher(Person existingPerson, Person incompletePerson) throws IllegalAccessException {
        Class<?> personClass = Person.class;
        Field[] personFields = personClass.getDeclaredFields();
        for (Field field : personFields) {
            field.setAccessible(true);
            Object value = field.get(incompletePerson);
            if (value != null) {
                field.set(existingPerson, value);
            }
            field.setAccessible(false);
        }
    }

    /**
     * PATCH-запрос для House.
     * Копирует значения непустых полей из incompletePerson в existingPerson.
     *
     * @param existingHouse   существующая сущность House
     * @param incompleteHouse сущность House с обновленными значениями
     * @throws IllegalAccessException если возникла ошибка при доступе к полю
     */
    public void housePatcher(House existingHouse, House incompleteHouse) throws IllegalAccessException {
        Class<?> houseClass = House.class;
        Field[] houseFields = houseClass.getDeclaredFields();
        for (Field field : houseFields) {
            field.setAccessible(true);
            Object value = field.get(incompleteHouse);
            if (value != null) {
                field.set(existingHouse, value);
            }
            field.setAccessible(false);
        }
    }
}
