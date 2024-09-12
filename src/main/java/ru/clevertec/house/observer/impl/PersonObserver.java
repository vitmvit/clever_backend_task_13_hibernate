package ru.clevertec.house.observer.impl;

import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.observer.Observer;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Класс PersonObserver реализует интерфейс Observer и представляет собой наблюдателя,
 * который следит за состоянием EventSource.
 */
public class PersonObserver implements Observer {

    @Override
    public void update(Object o) {
        if (o instanceof Person person) {
            person.setUuid(UUID.randomUUID());
            person.setHouses(new ArrayList<>());
        } else {
            throw new ClassCastException();
        }
    }
}
