package ru.clevertec.house.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.clevertec.house.model.dto.create.PersonCreateDto;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonUpdateDto extends PersonCreateDto {

    private UUID uuid;
}
