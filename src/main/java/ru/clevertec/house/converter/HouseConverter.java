package ru.clevertec.house.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HouseConverter {

    HouseDto convert(House source);

    House convert(HouseCreateDto source);

    House convert(HouseUpdateDto source);

    House merge(@MappingTarget House house, HouseUpdateDto dto);
}
