package ru.clevertec.house.strategy.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.PatchException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.strategy.HouseUpdateStrategy;

/**
 * Стратегия частичного обновления объекта House.
 * <p>
 * Данная стратегия позволяет обновлять только определенные поля
 * существующего объекта House, оставляя остальные поля без изменений.
 */
@Service
@AllArgsConstructor
public class HousePatchStrategy implements HouseUpdateStrategy {

    private final HouseRepository houseRepository;
    private final Patcher patcher;

    /**
     * Частично обновляет существующий объект House.
     *
     * @param house объект House с изменениями. Необязательные поля могут быть оставлены пустыми.
     * @return обновленный объект House.
     * @throws PatchException если возникла ошибка при применении обновлений.
     */
    @Override
    public House update(House house) {
        var existPerson = houseRepository.getByUuid(house.getUuid());
        try {
            patcher.housePatcher(existPerson, house);
            houseRepository.update(existPerson);
            return existPerson;
        } catch (IllegalAccessException e) {
            throw new PatchException();
        }
    }
}