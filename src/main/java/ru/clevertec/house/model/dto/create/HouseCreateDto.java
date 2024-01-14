package ru.clevertec.house.model.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.model.entity.Person;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseCreateDto {
    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;
    private List<Person> residents;
    private List<Person> owners;
}
