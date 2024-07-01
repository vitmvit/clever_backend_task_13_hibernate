package ru.clevertec.house.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.Person;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonConverter {
    PersonDto convert(Person source);

    Person convert(PersonCreateDto source);

    Person convert(PersonUpdateDto source);

    Person merge(@MappingTarget Person house, PersonUpdateDto dto);
}


