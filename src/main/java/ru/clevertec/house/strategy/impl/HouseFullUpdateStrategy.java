package ru.clevertec.house.strategy.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.strategy.HouseUpdateStrategy;

/**
 * Стратегия полного обновления объекта House.
 * <p>
 * Эта стратегия позволяет полностью обновить существующий
 * объект House на основе данных из переданного объекта.
 */
@Component
@AllArgsConstructor
public class HouseFullUpdateStrategy implements HouseUpdateStrategy {

    private final HouseRepository houseRepository;

    /**
     * Полностью обновляет существующий объект House.
     * <p>
     * Находит объект House по его UUID и заменяет его данными
     * из переданного объекта house.
     *
     * @param person объект House с новыми данными для обновления. Необходимый объект должен содержать действующий UUID.
     * @return обновленный объект House.
     */
    @Override
    public House update(House person) {
        var house = houseRepository.getByUuid(person.getUuid());
        return houseRepository.update(house);
    }
}