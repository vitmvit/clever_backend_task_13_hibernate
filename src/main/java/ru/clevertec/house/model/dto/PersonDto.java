package ru.clevertec.house.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.constant.SexType;
import ru.clevertec.house.model.entity.House;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Long id;
    private UUID uuid;
    private String name;
    private String surname;
    private SexType sex;
    private String passportSeries;
    private String passportNumber;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private House house;
    private boolean isOwner;
}