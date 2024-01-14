package ru.clevertec.house.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto {

    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;
}
