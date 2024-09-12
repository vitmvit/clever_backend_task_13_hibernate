package ru.clevertec.house.observer;

import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс EventSource представляет собой объект, за состоянием которого следят наблюдатели.
 * Он управляет списком наблюдателей и уведомляет их об изменениях.
 */
public class EventSource {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Добавляет наблюдателя в список.
     *
     * @param observer Наблюдатель, который должен быть добавлен.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Удаляет наблюдателя из списка.
     *
     * @param observer Наблюдатель, который должен быть удален.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Уведомляет всех зарегистрированных наблюдателей об изменении состояния.
     */
    public void notifyObservers(Person person) {
        for (Observer observer : observers) {
            observer.update(person);
        }
    }

    /**
     * Метод для изменения состояния Subject.
     * После изменения состояния, наблюдатели будут уведомлены.
     */
    public void notifyObservers(House house) {
        for (Observer observer : observers) {
            observer.update(house);
        }
    }
}