package ru.clevertec.house.observer.impl;

import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.observer.Observer;

import java.util.UUID;

/**
 * Класс HouseObserver реализует интерфейс Observer и представляет собой наблюдателя,
 * который следит за состоянием EventSource.
 */
public class HouseObserver implements Observer {

    @Override
    public void update(Object o) {
        if (o instanceof House house) {
            house.setUuid(UUID.randomUUID());
        } else {
            throw new ClassCastException();
        }
    }
}