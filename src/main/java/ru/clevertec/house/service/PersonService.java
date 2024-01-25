package ru.clevertec.house.service;

import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;

import java.util.List;
import java.util.UUID;

public interface PersonService {

    PersonDto getByUuid(UUID uuid);

    List<PersonDto> getAll(int offset, int limit);

    List<PersonDto> searchBySurname(String surname);

    PersonDto create(PersonCreateDto dto);

    PersonDto update(PersonUpdateDto dto);

    PersonDto patch(PersonUpdateDto personUpdateDto);

    void delete(UUID uuid);

    List<HouseDto> getAllHouses(UUID uuid);
}
