package ru.clevertec.house.service.impl;

import lombok.AllArgsConstructor;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.exception.ResourceNotFoundException;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.OwnerService;
import ru.clevertec.house.service.PersonService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final PersonService personService;
    private final HouseService houseService;
    private final PersonConverter personConverter;
    private final HouseConverter houseConverter;

    @Override
    public PersonDto getPersonByUUID(UUID uuid) {
        return personConverter.convert(personService.getByUUID(uuid));
    }

    @Override
    public List<PersonDto> getAllPersons() {
        List<Person> personList = personService.getAll();
        return personList.isEmpty()
                ? List.of()
                : personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<PersonDto> getAllPersons(UUID houseUuid) {
        var house = houseService.getByUUID(houseUuid);
        return house.getOwners().isEmpty()
                ? List.of()
                : house.getOwners().stream().map(personConverter::convert).collect(Collectors.toList());
    }

    @Override
    public PersonDto createPerson(PersonCreateDto dto) {
        try {
            var person = personConverter.convert(dto);
            person.setUuid(UUID.randomUUID());
            return personConverter.convert(personService.create(person));
        } catch (Exception e) {
            throw new ResourceNotFoundException(e);
        }
    }

    @Override
    public PersonDto updatePerson(PersonUpdateDto dto) {
        var person = personService.getByUUID(dto.getUuid());
        personConverter.merge(person, dto);
        return personConverter.convert(personService.update(person));
    }

    @Override
    public void deletePerson(UUID uuid) {
        personService.delete(uuid);
    }

    @Override
    public List<PersonDto> getPersonPage(int page, int count) {
        List<Person> allObjects = personService.getAll();
        allObjects.sort(Comparator.comparing(Person::getId));

        int startIndex = (page - 1) * count;
        int endIndex = Math.min(startIndex + count, allObjects.size());

        if (startIndex > allObjects.size()) {
            return new ArrayList<>();
        }

        List<Person> houseList = allObjects.subList(startIndex, Math.min(endIndex, allObjects.size()));
        return houseList.isEmpty() ? List.of() : houseList.stream().map(personConverter::convert).collect(Collectors.toList());
    }

    @Override
    public HouseDto getHouseByUUID(UUID uuid) {
        return houseConverter.convert(houseService.getByUUID(uuid));
    }

    @Override
    public List<HouseDto> getAllHouses() {
        List<House> houseList = houseService.getAll();
        return houseList.isEmpty()
                ? List.of()
                : houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<HouseDto> getAllHouses(UUID personUuid) {
        var person = personService.getByUUID(personUuid);
        return person.getHouse().isEmpty()
                ? List.of()
                : person.getHouse().stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    @Override
    public HouseDto createHouse(HouseCreateDto dto) {
        try {
            var house = houseConverter.convert(dto);
            house.setUuid(UUID.randomUUID());
            house.setCreateDate(LocalDateTime.now());
            return houseConverter.convert(houseService.create(house));
        } catch (Exception e) {
            throw new ResourceNotFoundException(e);
        }
    }

    @Override
    public HouseDto updateHouse(HouseUpdateDto dto) {
        var house = houseService.getByUUID(dto.getUuid());
        houseConverter.merge(house, dto);
        return houseConverter.convert(houseService.update(house));
    }

    @Override
    public void deleteHouse(UUID uuid) {
        houseService.delete(uuid);
    }

    @Override
    public List<HouseDto> getHousePage(int page, int count) {
        List<House> allObjects = houseService.getAll();
        allObjects.sort(Comparator.comparing(House::getId));

        int startIndex = (page - 1) * count;
        int endIndex = Math.min(startIndex + count, allObjects.size());

        if (startIndex > allObjects.size()) {
            return new ArrayList<>();
        }

        List<House> houseList = allObjects.subList(startIndex, Math.min(endIndex, allObjects.size()));
        return houseList.isEmpty() ? List.of() : houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }
}
