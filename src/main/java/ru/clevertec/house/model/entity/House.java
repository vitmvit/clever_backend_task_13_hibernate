package ru.clevertec.house.model.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.model.parent.BaseModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class House extends BaseModel {

    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;
    private List<Person> residents;
    private List<Person> owners;
}
