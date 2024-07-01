package ru.clevertec.house.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.model.dto.create.HouseCreateDto;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HouseUpdateDto extends HouseCreateDto {

    private UUID uuid;
}
