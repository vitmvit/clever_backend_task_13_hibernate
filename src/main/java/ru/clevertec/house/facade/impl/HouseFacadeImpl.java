package ru.clevertec.house.facade.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.clevertec.house.converter.HouseConverter;
import ru.clevertec.house.converter.PersonConverter;
import ru.clevertec.house.facade.HouseFacade;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.service.HouseService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class HouseFacadeImpl implements HouseFacade {

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseConverter houseConverter;

    @Autowired
    private PersonConverter personConverter;

    @Override
    public HouseDto getByUuid(UUID uuid) {
        return houseConverter.convert(houseService.getByUuid(uuid));
    }

    @Override
    public List<HouseDto> getAll(int offset, int limit) {
        List<House> housePage = houseService.getAll(offset, limit);
        return housePage.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<HouseDto> searchByCity(String city) {
        var houseList = houseService.searchByCity(city);
        return houseList.stream().map(houseConverter::convert).collect(Collectors.toList());
    }

    @Override
    public HouseDto create(HouseCreateDto dto) {
        return houseConverter.convert(houseService.create(houseConverter.convert(dto)));
    }

    @Override
    public HouseDto update(HouseUpdateDto dto) {
        var house = houseService.getByUuid(dto.getUuid());
        houseConverter.merge(house, dto);
        return houseConverter.convert(houseService.update(house));
    }

    @Override
    public HouseDto patch(HouseUpdateDto houseUpdateDto) {
        return houseConverter.convert(houseService.patch(houseConverter.convert(houseUpdateDto)));
    }

    @Override
    public void delete(UUID uuid) {
        houseService.delete(uuid);
    }

    @Override
    public List<PersonDto> getAllResidents(UUID uuid) {
        var personList = houseService.getAllResidents(uuid);
        return personList.stream().map(personConverter::convert).collect(Collectors.toList());
    }
}