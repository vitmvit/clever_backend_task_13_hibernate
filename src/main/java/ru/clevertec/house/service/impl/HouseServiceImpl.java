package ru.clevertec.house.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.EmptyListException;
import ru.clevertec.house.exception.EntityNotFoundException;
import ru.clevertec.house.exception.PatchException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.util.Patcher;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private Patcher patcher;

    /**
     * Возвращает информацию о доме по заданному UUID.
     *
     * @param uuid UUID дома
     * @return информация о доме
     * @throws EntityNotFoundException если дом не найден
     */
    @Override
    public House getByUuid(UUID uuid) {
        return houseRepository.getByUuid(uuid);
    }

    /**
     * Возвращает страницу с информацией о домах.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о домах
     */
    @Override
    public List<House> getAll(int offset, int limit) {
        List<House> housePage = houseRepository.getAll(offset, limit);
        return housePage.isEmpty()
                ? List.of()
                : housePage;
    }

    /**
     * Возвращает список домов, найденных по фрагменту названия города.
     *
     * @param city фрагмент названия города
     * @return список домов
     * @throws EmptyListException если список домов пуст
     */
    @Override
    public List<House> searchByCity(String city) {
        var houseList = houseRepository.getByCityContaining(city);
        houseList.stream().findAny().orElseThrow(EmptyListException::new);
        return houseList;
    }

    /**
     * Создает новый дом и сохраняет его в репозитории.
     *
     * @param house объект дома, который необходимо создать
     * @return созданный объект дома
     */
    @Override
    public House create(House house) {
        return houseRepository.create(house);
    }

    /**
     * Обновляет существующий дом в репозитории.
     *
     * @param dto объект дома, содержащий обновленные данные
     * @return обновленный объект дома
     */
    @Override
    public House update(House dto) {
        var house = houseRepository.getByUuid(dto.getUuid());
        return houseRepository.update(house);
    }

    /**
     * Частично обновляет существующий дом в репозитории.
     *
     * @param houseUpdateDto объект дома, содержащий данные для частичного обновления
     * @return частично обновленный объект дома
     * @throws PatchException если произошла ошибка при частичном обновлении
     */
    @Override
    public House patch(House houseUpdateDto) {
        var house = houseRepository.getByUuid(houseUpdateDto.getUuid());
        try {
            patcher.housePatcher(house, houseUpdateDto);
            houseRepository.update(house);
            return house;
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
        houseRepository.deleteByUuid(uuid);
    }

    /**
     * Возвращает список проживающих лиц в доме по заданному UUID.
     *
     * @param uuid UUID дома
     * @return список проживающих лиц
     */
    @Override
    public List<Person> getAllResidents(UUID uuid) {
        var house = houseRepository.getByUuid(uuid);
        return house.getResidents().isEmpty()
                ? List.of()
                : house.getResidents();
    }
}