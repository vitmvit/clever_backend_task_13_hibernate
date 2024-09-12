package ru.clevertec.house.strategy.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.house.exception.PatchException;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.patcher.Patcher;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.strategy.PersonUpdateStrategy;

/**
 * Стратегия частичного обновления объекта Person.
 * <p>
 * Данная стратегия позволяет обновлять только определенные поля
 * существующего объекта Person, оставляя остальные поля без изменений.
 */
@Component
@AllArgsConstructor
public class PersonPatchStrategy implements PersonUpdateStrategy {

    private final PersonRepository personRepository;
    private final Patcher patcher;

    /**
     * Частично обновляет существующий объект Person.
     *
     * @param person объект Person с изменениями. Необязательные поля могут быть оставлены пустыми.
     * @return обновленный объект Person.
     * @throws PatchException если возникла ошибка при применении обновлений.
     */
    @Override
    public Person update(Person person) {
        var existPerson = personRepository.getByUuid(person.getUuid());
        try {
            patcher.personPatcher(existPerson, person);
            personRepository.update(existPerson);
            return existPerson;
        } catch (IllegalAccessException e) {
            throw new PatchException();
        }
    }
}