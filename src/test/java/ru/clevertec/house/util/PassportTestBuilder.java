package ru.clevertec.house.util;

import lombok.Builder;
import ru.clevertec.house.model.dto.PassportDto;
import ru.clevertec.house.model.entity.Passport;

@Builder(setterPrefix = "with")
public class PassportTestBuilder {

    @Builder.Default
    private String passportSeries = "HJ";

    @Builder.Default
    private String passportNumber = "9874563";

    public Passport buildPassport() {
        return new Passport(passportSeries, passportNumber);
    }

    public PassportDto buildPassportDto() {
        return new PassportDto(passportSeries, passportNumber);
    }
}
