package ru.clevertec.house.model.dto.create;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.constant.Sex;
import ru.clevertec.house.model.entity.Passport;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonCreateDto {

    private String name;
    private String surname;
    private Sex sex;

    @Embedded
    private Passport passport;
    private UUID homeUuid;
}
