package ru.clevertec.house.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.facade.HouseFacade;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.service.HouseService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс HouseFacadeImpl предоставляет упрощённый интерфейс для работы с
 * объектами дома и их состоянием.
 */
@Component
public class HouseFacadeImpl implements HouseFacade {

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseConverter houseConverter;

    @Autowired
    private PersonConverter personConverter;

    /**
     * Получает дом по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     * @return объект HouseDto, представляющий дом
     */
    @Override
    public HouseDto getByUuid(UUID uuid) {
        return houseConverter.convert(houseService.getByUuid(uuid));
    }

    /**
     * Получает список всех домов с применением пагинации.
     *
     * @param offset сдвиг для пагинации
     * @param limit  максимальное количество возвращаемых домов
     * @return список HouseDto, представляющий дома
     */
    @Override
    public List<HouseDto> getAll(int offset, int limit) {
        List<House> housePage = houseService.getAll(offset, limit);
        return housePage.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    /**
     * Ищет дома по названию города.
     *
     * @param city название города
     * @return список HouseDto, представляющий найденные дома
     */
    @Override
    public List<HouseDto> searchByCity(String city) {
        var houseList = houseService.searchByCity(city);
        return houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает новый дом на основе переданных данных.
     *
     * @param dto объект HouseCreateDto, содержащий данные для создания дома
     * @return объект HouseDto, представляющий созданный дом
     */
    @Override
    public HouseDto create(HouseCreateDto dto) {
        return houseConverter.convert(houseService.create(houseConverter.convert(dto)));
    }

    /**
     * Обновляет существующий дом на основе переданных данных.
     *
     * @param dto объект HouseUpdateDto, содержащий данные для обновления дома
     * @return объект HouseDto, представляющий обновленный дом
     */
    @Override
    public HouseDto update(HouseUpdateDto dto) {
        var house = houseService.getByUuid(dto.getUuid());
        houseConverter.merge(house, dto);
        return houseConverter.convert(houseService.update(house));
    }

    /**
     * Частично обновляет дом на основе переданных данных.
     *
     * @param houseUpdateDto объект HouseUpdateDto, содержащий данные для частичного обновления дома
     * @return объект HouseDto, представляющий обновленный дом
     */
    @Override
    public HouseDto patch(HouseUpdateDto houseUpdateDto) {
        return houseConverter.convert(houseService.patch(houseConverter.convert(houseUpdateDto)));
    }

    /**
     * Удаляет дом по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     */
    @Override
    public void delete(UUID uuid) {
        houseService.delete(uuid);
    }

    /**
     * Получает всех жителей дома по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     * @return список PersonDto, представляющий жителей дома
     */
    @Override
    public List<PersonDto> getAllResidents(UUID uuid) {
        var personList = houseService.getAllResidents(uuid);
        return personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }
}