package ru.clevertec.house.facade.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.facade.PersonFacade;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.PersonService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Фасад для управления данными о людях, предоставляющий упрощенный интерфейс
 * для операций, таких как получение, создание и обновление информации о людях.
 */
@Component
@AllArgsConstructor
public class PersonFacadeImpl implements PersonFacade {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonConverter personConverter;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseConverter houseConverter;

    /**
     * Получает человека по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     * @return объект PersonDto, представляющий человека
     */
    @Override
    public PersonDto getByUuid(UUID uuid) {
        return personConverter.convert(personService.getByUuid(uuid));
    }

    /**
     * Получает список всех людей с применением пагинации.
     *
     * @param offset сдвиг для пагинации
     * @param limit  максимальное количество возвращаемых людей
     * @return список PersonDto, представляющий людей
     */
    @Override
    public List<PersonDto> getAll(int offset, int limit) {
        List<Person> personPage = personService.getAll(offset, limit);
        return personPage.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    /**
     * Ищет людей по фамилии.
     *
     * @param surname фамилия человека
     * @return список PersonDto, представляющий найденных людей
     */
    @Override
    public List<PersonDto> searchBySurname(String surname) {
        var personList = personService.searchBySurname(surname);
        return personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает нового человека на основе переданных данных.
     *
     * @param dto объект PersonCreateDto, содержащий данные для создания человека
     * @return объект PersonDto, представляющий созданного человека
     */
    @Override
    public PersonDto create(PersonCreateDto dto) {
        var person = personConverter.convert(dto);
        var house = houseService.getByUuid(dto.getHomeUuid());
        person.setHome(house);
        person = personService.create(person);
        return personConverter.convert(person);
    }

    /**
     * Обновляет существующего человека на основе переданных данных.
     *
     * @param dto объект PersonUpdateDto, содержащий данные для обновления человека
     * @return объект PersonDto, представляющий обновленного человека
     */
    @Override
    public PersonDto update(PersonUpdateDto dto) {
        var person = personService.getByUuid(dto.getUuid());
        personConverter.merge(person, dto);
        return personConverter.convert(personService.update(person));
    }

    /**
     * Частично обновляет человека на основе переданных данных.
     *
     * @param personUpdateDto объект PersonUpdateDto, содержащий данные для частичного обновления человека
     * @return объект PersonDto, представляющий обновленного человека
     */
    @Override
    public PersonDto patch(PersonUpdateDto personUpdateDto) {
        return personConverter.convert(personService.patch(personConverter.convert(personUpdateDto)));
    }

    /**
     * Удаляет человека по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     */
    @Override
    public void delete(UUID uuid) {
        personService.delete(uuid);
    }

    /**
     * Получает все дома, которыми владеет человек, по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     * @return список HouseDto, представляющий дома, принадлежащие человеку
     */
    @Override
    public List<HouseDto> getAllHouses(UUID uuid) {
        var houseList = personService.getAllHouses(uuid);
        return houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }
}