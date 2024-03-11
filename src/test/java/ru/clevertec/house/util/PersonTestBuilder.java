package ru.clevertec.house.util;

import lombok.Builder;
import ru.clevertec.house.constant.Sex;
import ru.clevertec.house.model.dto.PersonDto;
import ru.clevertec.house.model.dto.create.PersonCreateDto;
import ru.clevertec.house.model.dto.update.PersonUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Passport;
import ru.clevertec.house.model.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class PersonTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = UUID.fromString("c7c7b109-1128-4f32-b8f9-e1160530c2ad");

    @Builder.Default
    private String name = "NameOne";

    @Builder.Default
    private String surname = "SurnameOne";

    @Builder.Default
    private Sex sex = Sex.FEMALE;

    @Builder.Default
    private Passport passport = PassportTestBuilder.builder().build().buildPassport();

    @Builder.Default
    private House home = HouseTestBuilder.builder().build().buildHouse();

    @Builder.Default
    private List<House> houses = List.of(new House(), new House());

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(2024, 1, 22, 5, 34, 45, 23);

    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.of(2024, 1, 22, 5, 34, 45, 23);

    @Builder.Default
    private UUID homeUuid = UUID.fromString("7af79b15-d470-457d-9d76-ef4d248c816c");


    public Person buildPerson() {
        var person = new Person(name, surname, sex, passport, home, houses);
        person.setId(id);
        person.setUuid(uuid);
        person.setCreateDate(createDate);
        person.setUpdateDate(updateDate);
        return person;
    }

    public PersonDto buildPersonDto() {
        var personDto = new PersonDto(name, surname, sex, PassportTestBuilder.builder().build().buildPassportDto(), createDate, updateDate);
        personDto.setUuid(uuid);
        return personDto;
    }

    public PersonCreateDto buildPersonCreateDto() {
        return new PersonCreateDto(name, surname, sex, passport, homeUuid);
    }

    public PersonUpdateDto buildPersonUpdateDto() {
        var personUpdateDto = new PersonUpdateDto();
        personUpdateDto.setUuid(uuid);
        personUpdateDto.setName(name + "New");
        personUpdateDto.setSurname(surname + "New");
        personUpdateDto.setSex(sex);
        personUpdateDto.setPassport(passport);
        personUpdateDto.setHomeUuid(homeUuid);
        return personUpdateDto;
    }

    public UUID getUuid() {
        return UUID.fromString("8a131fbc-38bf-4689-8f0b-958cef82a3ef");
    }
}
