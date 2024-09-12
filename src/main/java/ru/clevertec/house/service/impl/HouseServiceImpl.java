package ru.clevertec.house.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.EmptyListException;
import ru.clevertec.house.exception.PatchException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.observer.EventSource;
import ru.clevertec.house.observer.impl.HouseObserver;
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
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private Patcher patcher;

    private final EventSource eventSource = new EventSource();

    public HouseServiceImpl() {
        eventSource.addObserver(new HouseObserver());
    }

    /**
     * Получает дом по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     * @return объект House, представляющий дом
     */
    @Override
    public House getByUuid(UUID uuid) {
        return houseRepository.getByUuid(uuid);
    }

    /**
     * Получает список всех домов с применением пагинации.
     *
     * @param offset сдвиг для пагинации
     * @param limit  максимальное количество возвращаемых домов
     * @return список House, представляющий дома
     */
    @Override
    public List<House> getAll(int offset, int limit) {
        List<House> housePage = houseRepository.getAll(offset, limit);
        return housePage.isEmpty()
                ? List.of()
                : housePage;
    }

    /**
     * Ищет дома по названию города.
     *
     * @param city название города
     * @return список House, представляющий найденные дома
     * @throws EmptyListException если не найдено ни одного дома
     */
    @Override
    public List<House> searchByCity(String city) {
        var houseList = houseRepository.getByCityContaining(city);
        houseList.stream().findAny().orElseThrow(EmptyListException::new);
        return houseList;
    }

    /**
     * Создает новый дом и уведомляет наблюдателей об этом событии.
     *
     * @param house объект House, содержащий данные для создания дома
     * @return объект House, представляющий созданный дом
     */
    @Override
    public House create(House house) {
        eventSource.notifyObservers(house);
        return houseRepository.create(house);
    }

    /**
     * Обновляет существующий дом.
     *
     * @param dto объект House, содержащий данные для обновления дома
     * @return объект House, представляющий обновленный дом
     */
    @Override
    public House update(House dto) {
        var house = houseRepository.getByUuid(dto.getUuid());
        return houseRepository.update(house);
    }

    /**
     * Частично обновляет дом на основе переданных данных.
     *
     * @param houseUpdateDto объект House, содержащий данные для частичного обновления дома
     * @return объект House, представляющий обновленный дом
     * @throws PatchException если происходит ошибка при обновлении
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
     * Удаляет дом по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     */
    @Override
    public void delete(UUID uuid) {
        houseRepository.deleteByUuid(uuid);
    }

    /**
     * Получает всех жителей дома по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор дома
     * @return список Person, представляющий жителей дома
     */
    @Override
    public List<Person> getAllResidents(UUID uuid) {
        var house = houseRepository.getByUuid(uuid);
        return house.getResidents().isEmpty()
                ? List.of()
                : house.getResidents();
    }
}