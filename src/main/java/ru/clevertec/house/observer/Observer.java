package ru.clevertec.house.observer;

/**
 * Интерфейс Observer, который должны реализовать все наблюдатели.
 */
public interface Observer {

    /**
     * Метод, который будет вызван, когда наблюдатель будет уведомлен об изменении.
     */
    void update(Object o);
}