package ru.clevertec.house.model.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.constant.SexType;
import ru.clevertec.house.model.parent.BaseModel;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Person extends BaseModel {

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
