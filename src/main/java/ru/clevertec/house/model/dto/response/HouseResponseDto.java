package ru.clevertec.house.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HouseResponseDto {

    private UUID uuid;
    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;
}
