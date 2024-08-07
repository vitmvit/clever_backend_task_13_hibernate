package ru.clevertec.house.model.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseCreateDto {

    private String area;
    private String country;
    private String city;
    private String street;
    private int number;
}
