package ru.clevertec.house.service;

import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OwnerService {

    PersonDto getPersonByUUID(UUID uuid);

    List<PersonDto> getAllPersons();

    List<PersonDto> getAllPersons(UUID houseUuid);

    PersonDto createPerson(PersonCreateDto dto);

    PersonDto updatePerson(PersonUpdateDto dto);

    void deletePerson(UUID uuid);

    List<PersonDto> getPersonPage(int page, int count);

    HouseDto getHouseByUUID(UUID uuid);

    List<HouseDto> getAllHouses();

    List<HouseDto> getAllHouses(UUID personUuid);

    HouseDto createHouse(HouseCreateDto dto);

    HouseDto updateHouse(HouseUpdateDto dto);

    void deleteHouse(UUID uuid);

    List<HouseDto> getHousePage(int page, int count);
}
