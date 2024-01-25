package ru.clevertec.house.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.exception.EmptyListException;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.PatchException;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.util.Patcher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс HouseServiceImpl реализует интерфейс HouseService и предоставляет методы для работы House
 *
 * @author Витикова Мария
 * @see ru.clevertec.house.service.HouseService
 */
@Service
@Transactional
@AllArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final HouseConverter houseConverter;
    private final PersonConverter personConverter;
    private final Patcher patcher;

    /**
     * Возвращает информацию о доме по заданному UUID.
     *
     * @param uuid UUID дома
     * @return информация о доме
     * @throws EntityNotFoundException если дом не найден
     */
    @Override
    public HouseDto getByUuid(UUID uuid) {
        return houseConverter.convert(houseRepository.getByUUID(uuid));
    }

    /**
     * Возвращает страницу с информацией о домах.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о домах
     */
    @Override
    public List<HouseDto> getAll(int offset, int limit) {
        List<House> housePage = houseRepository.getAll(offset, limit);
        return housePage.isEmpty()
                ? List.of()
                : housePage.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    /**
     * Возвращает список домов, найденных по фрагменту названия города.
     *
     * @param city фрагмент названия города
     * @return список домов
     * @throws EmptyListException если список домов пуст
     */
    @Override
    public List<HouseDto> searchByCity(String city) {
        var houseList = houseRepository.getBySurnameContaining(city);
        houseList.stream().findAny().orElseThrow(EmptyListException::new);
        return houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает новый дом на основе данных из DTO.
     *
     * @param dto данные для создания дома
     * @return созданный дом
     */
    @Override
    public HouseDto create(HouseCreateDto dto) {
        var house = houseConverter.convert(dto);
        return houseConverter.convert(houseRepository.create(house));
    }

    /**
     * Обновляет информацию о доме на основе данных из DTO.
     *
     * @param dto данные для обновления дома
     * @return обновленный дом
     * @throws EntityNotFoundException если дом не найден
     */
    @Override
    public HouseDto update(HouseUpdateDto dto) {
        var house = houseRepository.getByUUID(dto.getUuid());
        houseConverter.merge(house, dto);
        return houseConverter.convert(houseRepository.update(house));
    }

    /**
     * Обновляет информацию о жилом доме на основе данных из DTO.
     *
     * @param houseUpdateDto данные для обновления дома
     * @return обновленный дом
     * @throws EntityNotFoundException если жилой дом не найден
     * @throws PatchException          если возникла ошибка выполнении метода housePatcher
     * @see ru.clevertec.house.util.Patcher
     */
    @Override
    public HouseDto patch(HouseUpdateDto houseUpdateDto) {
        var house = houseRepository.getByUUID(houseUpdateDto.getUuid());
        try {
            patcher.housePatcher(house, houseConverter.convert(houseUpdateDto));
            houseRepository.update(house);
            return houseConverter.convert(house);
        } catch (IllegalAccessException e) {
            throw new PatchException();
        }
    }

    /**
     * Удаляет дом по заданному UUID.
     *
     * @param uuid UUID дома
     */
    @Override
    public void delete(UUID uuid) {
        houseRepository.deleteByUUID(uuid);
    }

    /**
     * Возвращает список проживающих лиц в доме по заданному UUID.
     *
     * @param uuid UUID дома
     * @return список проживающих лиц
     * @throws EntityNotFoundException если дом не найден
     */
    @Override
    public List<PersonDto> getAllResidents(UUID uuid) {
        var house = houseRepository.getByUUID(uuid);
        return house.getResidents().isEmpty()
                ? List.of()
                : house.getResidents().stream().map(personConverter::convert).collect(Collectors.toList());
    }
}