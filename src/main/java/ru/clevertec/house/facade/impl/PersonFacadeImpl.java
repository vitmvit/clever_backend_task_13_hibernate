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

    @Override
    public PersonDto getByUuid(UUID uuid) {
        return personConverter.convert(personService.getByUuid(uuid));
    }

    @Override
    public List<PersonDto> getAll(int offset, int limit) {
        List<Person> personPage = personService.getAll(offset, limit);
        return personPage.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> searchBySurname(String surname) {
        var personList = personService.searchBySurname(surname);
        return personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    @Override
    public PersonDto create(PersonCreateDto dto) {
        var person = personConverter.convert(dto);
        var house = houseService.getByUuid(dto.getHomeUuid());
        person.setHome(house);
        person = personService.create(person);
        return personConverter.convert(person);
    }

    @Override
    public PersonDto update(PersonUpdateDto dto) {
        var person = personService.getByUuid(dto.getUuid());
        personConverter.merge(person, dto);
        return personConverter.convert(personService.update(person));
    }

    @Override
    public PersonDto patch(PersonUpdateDto personUpdateDto) {
        return personConverter.convert(personService.patch(personConverter.convert(personUpdateDto)));
    }

    @Override
    public void delete(UUID uuid) {
        personService.delete(uuid);
    }

    @Override
    public List<HouseDto> getAllHouses(UUID uuid) {
        var houseList = personService.getAllHouses(uuid);
        return houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }
}