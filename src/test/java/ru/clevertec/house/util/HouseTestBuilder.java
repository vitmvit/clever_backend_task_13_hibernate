package ru.clevertec.house.util;

import lombok.Builder;
import ru.clevertec.house.model.dto.HouseDto;
import ru.clevertec.house.model.dto.create.HouseCreateDto;
import ru.clevertec.house.model.dto.update.HouseUpdateDto;
import ru.clevertec.house.model.entity.House;
import ru.clevertec.house.model.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class HouseTestBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = UUID.fromString("8a131fbc-38bf-4689-8f0b-958cef82a3ef");

    @Builder.Default
    private String area = "AreaOne";

    @Builder.Default
    private String country = "Country";

    @Builder.Default
    private String city = "CityTwo";

    @Builder.Default
    private String street = "StreetThree";

    @Builder.Default
    private int number = 67;

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.of(2024, 1, 3, 9, 12, 15, 156);

    @Builder.Default
    private List<Person> residents = List.of(new Person(), new Person());

    @Builder.Default
    private List<Person> owners = List.of();

    public House buildHouse() {
        var house = new House(area, country, city, street, number, createDate, residents, owners);
        house.setId(id);
        house.setUuid(uuid);
        return house;
    }

    public HouseDto buildHouseDto() {
        var houseDto = new HouseDto(area, country, city, street, number, createDate);
        houseDto.setUuid(uuid);
        return houseDto;
    }

    public HouseCreateDto buildHouseCreateDto() {
        return new HouseCreateDto(area, country, city, street, number);
    }

    public HouseUpdateDto buildHouseUpdateDto() {
        var houseUpdateDto = new HouseUpdateDto(uuid);
        houseUpdateDto.setArea(area + "New");
        houseUpdateDto.setCountry(country);
        houseUpdateDto.setCity(city);
        houseUpdateDto.setStreet(street);
        houseUpdateDto.setNumber(number);
        return houseUpdateDto;
    }

    public UUID getUuid() {
        return UUID.fromString("8a131fbc-38bf-4689-8f0b-958cef82a3ef");
    }
}