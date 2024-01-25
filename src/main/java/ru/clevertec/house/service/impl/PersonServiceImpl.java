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
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.Patcher;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonConverter personConverter;
    private final HouseConverter houseConverter;
    private final HouseRepository houseRepository;

    private final Patcher patcher;

    /**
     * Возвращает информацию о жильце по заданному UUID.
     *
     * @param uuid UUID жильца
     * @return информация о жильце
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public PersonDto getByUuid(UUID uuid) {
        return personConverter.convert(personRepository.getByUUID(uuid));
    }

    /**
     * Возвращает страницу с информацией о жильцах.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о жильцах
     */
    @Override
    public List<PersonDto> getAll(int offset, int limit) {
        List<Person> personPage = personRepository.getAll(offset, limit);
        return personPage.isEmpty()
                ? List.of()
                : personPage.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    /**
     * Возвращает список жильцав, найденных по фрагменту фамилии.
     *
     * @param surname фрагмент фамилии
     * @return список жильцав
     * @throws EmptyListException если список жильцав пуст
     */
    @Override
    public List<PersonDto> searchBySurname(String surname) {
        var personList = personRepository.getByCityContaining(surname);
        personList.stream().findAny().orElseThrow(EmptyListException::new);
        return personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    /**
     * Создает нового жильца на основе данных из DTO.
     *
     * @param dto данные для создания жильца
     * @return созданный жилец
     */
    @Override
    public PersonDto create(PersonCreateDto dto) {
        var person = personConverter.convert(dto);
        person.setHome(houseRepository.getByUUID(dto.getHomeUuid()));
        return personConverter.convert(personRepository.create(person));
    }

    /**
     * Обновляет информацию о жильце на основе данных из DTO.
     *
     * @param dto данные для обновления жильца
     * @return обновленный жилец
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public PersonDto update(PersonUpdateDto dto) {
        var person = personRepository.getByUUID(dto.getUuid());
        personConverter.merge(person, dto);
        return personConverter.convert(personRepository.update(person));
    }

    /**
     * Обновляет информацию о жильце на основе данных из DTO.
     *
     * @param personUpdateDto данные для обновления жильца
     * @return обновленный жилец
     * @throws EntityNotFoundException если жильце не найден
     * @throws PatchException          если возникла ошибка выполнении метода personPatcher
     * @see ru.clevertec.house.util.Patcher
     */
    @Override
    public PersonDto patch(PersonUpdateDto personUpdateDto) {
        var person = personRepository.getByUUID(personUpdateDto.getUuid());
        try {
            patcher.personPatcher(person, personConverter.convert(personUpdateDto));
            personRepository.update(person);
            return personConverter.convert(person);
        } catch (IllegalAccessException e) {
            throw new PatchException();
        }
    }

    /**
     * Удаляет жильца по заданному UUID.
     *
     * @param uuid UUID жильца
     */
    @Override
    public void delete(UUID uuid) {
        personRepository.deleteByUUID(uuid);
    }

    /**
     * Получает список всех домов, связанных с указанным жильцом.
     *
     * @param uuid UUID жильцом
     * @return список домов
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public List<HouseDto> getAllHouses(UUID uuid) {
        var person = personRepository.getByUUID(uuid);
        return person.getHouses().isEmpty()
                ? List.of()
                : person.getHouses().stream().map(houseConverter::convert).collect(Collectors.toList());
    }
}
