package ru.clevertec.house.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.model.entity.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {

    private Long id;
    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;
    private List<Person> residents;
    private List<Person> owners;
}
