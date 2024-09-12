package ru.clevertec.house.strategy.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.repository.PersonRepository;

/**
 * Стратегия полного обновления объекта Person.
 * <p>
 * Эта стратегия позволяет полностью обновить существующий
 * объект Person на основе данных из переданного объекта.
 */
@Component
@AllArgsConstructor
public class PersonFullUpdateStrategy implements ru.clevertec.house.strategy.PersonUpdateStrategy {

    private final PersonRepository personRepository;

    /**
     * Полностью обновляет существующий объект Person.
     *
     * @param person объект Person с новыми данными для обновления. Необходимый объект должен содержать действующий UUID.
     * @return обновленный объект Person.
     */
    @Override
    public Person update(Person person) {
        return personRepository.update(personRepository.getByUuid(person.getUuid()));
    }
}