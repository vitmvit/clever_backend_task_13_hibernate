package ru.clevertec.house.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.house.exception.EmptyListException;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.observer.EventSource;
import ru.clevertec.house.observer.impl.PersonObserver;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.strategy.PersonUpdateStrategy;
import ru.clevertec.house.strategy.impl.PersonFullUpdateStrategy;
import ru.clevertec.house.strategy.impl.PersonPatchStrategy;

import java.util.List;
import java.util.UUID;

/**
 * Реализация сервиса управления данными о людях, основанная на репозиториях
 * для выполнения различных операций с людьми.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final EventSource eventSource;
    private PersonUpdateStrategy personUpdateStrategy;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Patcher patcher;

    public PersonServiceImpl() {
        eventSource = new EventSource();
        eventSource.addObserver(new PersonObserver());
    }

    /**
     * Получает человека по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     * @return объект Person, представляющий человека
     */
    @Override
    public Person getByUuid(UUID uuid) {
        return personRepository.getByUuid(uuid);
    }

    /**
     * Получает список всех людей с применением пагинации.
     *
     * @param offset сдвиг для пагинации
     * @param limit  максимальное количество возвращаемых людей
     * @return список Person, представляющий людей
     */
    @Override
    public List<Person> getAll(int offset, int limit) {
        List<Person> personPage = personRepository.getAll(offset, limit);
        return personPage.isEmpty()
                ? List.of()
                : personPage;
    }

    /**
     * Ищет людей по фамилии.
     *
     * @param surname фамилия человека
     * @return список Person, представляющий найденных людей
     * @throws EmptyListException если не найдено ни одного человека
     */
    @Override
    public List<Person> searchBySurname(String surname) {
        var personList = personRepository.getBySurnameContaining(surname);
        personList.stream().findAny().orElseThrow(EmptyListException::new);
        return personList;
    }

    /**
     * Создает нового человека и уведомляет наблюдателей об этом событии.
     *
     * @param person объект Person, содержащий данные для создания человека
     * @return объект Person, представляющий созданного человека
     */
    @Override
    public Person create(Person person) {
        eventSource.notifyObservers(person);
        return personRepository.create(person);
    }

    /**
     * Обновляет объект Person с использованием стратегии полного обновления.
     * <p>
     * Создает новую стратегию {@link PersonFullUpdateStrategy} и вызывает
     * её метод обновления для обновления переданного объекта person.
     *
     * @param person объект Person с новыми данными для обновления.
     * @return обновленный объект Person.
     */
    @Override
    public Person update(Person person) {
        personUpdateStrategy = new PersonFullUpdateStrategy(personRepository);
        return personUpdateStrategy.update(person);
    }

    /**
     * Частично обновляет объект Person с использованием стратегии патчинга.
     * <p>
     * Создает новую стратегию {@link PersonPatchStrategy} и вызывает её метод
     * обновления для применения изменений к переданному объекту person.
     *
     * @param person объект Person с изменениями.
     *               Необязательные поля могут быть оставлены пустыми.
     * @return обновленный объект Person.
     */
    @Override
    public Person patch(Person person) {
        personUpdateStrategy = new PersonPatchStrategy(personRepository, patcher);
        return personUpdateStrategy.update(person);
    }

    /**
     * Удаляет человека по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     */
    @Override
    public void delete(UUID uuid) {
        personRepository.deleteByUuid(uuid);
    }

    /**
     * Получает все дома, которыми владеет человек, по его уникальному идентификатору.
     *
     * @param uuid уникальный идентификатор человека
     * @return список House, представляющий дома, принадлежащие человеку
     */
    @Override
    public List<House> getAllHouses(UUID uuid) {
        var person = personRepository.getByUuid(uuid);
        return person.getHouses().isEmpty()
                ? List.of()
                : person.getHouses();
    }
}