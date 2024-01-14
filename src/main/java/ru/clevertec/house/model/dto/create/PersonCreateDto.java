package ru.clevertec.house.model.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.constant.SexType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDto {

    private String name;
    private String surname;
    private SexType sex;
    private String passportSeries;
    private String passportNumber;
}
