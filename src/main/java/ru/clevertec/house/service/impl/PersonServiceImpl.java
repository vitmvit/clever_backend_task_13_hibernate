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
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.util.Patcher;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Patcher patcher;

    /**
     * Возвращает информацию о жильце по заданному UUID.
     *
     * @param uuid UUID жильца
     * @return информация о жильце
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public Person getByUuid(UUID uuid) {
        return personRepository.getByUuid(uuid);
    }

    /**
     * Возвращает страницу с информацией о жильцах.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о жильцах
     */
    @Override
    public List<Person> getAll(int offset, int limit) {
        List<Person> personPage = personRepository.getAll(offset, limit);
        return personPage.isEmpty()
                ? List.of()
                : personPage;
    }

    /**
     * Возвращает список жильцав, найденных по фрагменту фамилии.
     *
     * @param surname фрагмент фамилии
     * @return список жильцав
     * @throws EmptyListException если список жильцав пуст
     */
    @Override
    public List<Person> searchBySurname(String surname) {
        var personList = personRepository.getBySurnameContaining(surname);
        personList.stream().findAny().orElseThrow(EmptyListException::new);
        return personList;
    }

    /**
     * Создает нового жильца на основе данных из DTO.
     *
     * @param person данные для создания жильца
     * @return созданный жилец
     */
    @Override
    public Person create(Person person) {
        return personRepository.create(person);
    }

    /**
     * Обновляет информацию о жильце на основе данных из DTO.
     *
     * @param dto данные для обновления жильца
     * @return обновленный жилец
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public Person update(Person dto) {
        var person = personRepository.getByUuid(dto.getUuid());
        return personRepository.update(person);
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
    public Person patch(Person personUpdateDto) {
        var person = personRepository.getByUuid(personUpdateDto.getUuid());
        try {
            patcher.personPatcher(person, personUpdateDto);
            personRepository.update(person);
            return person;
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
        personRepository.deleteByUuid(uuid);
    }

    /**
     * Получает список всех домов, связанных с указанным жильцом.
     *
     * @param uuid UUID жильцом
     * @return список домов
     * @throws EntityNotFoundException если жилец не найден
     */
    @Override
    public List<House> getAllHouses(UUID uuid) {
        var person = personRepository.getByUuid(uuid);
        return person.getHouses().isEmpty()
                ? List.of()
                : person.getHouses();
    }
}