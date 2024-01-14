package ru.clevertec.house.model.parent;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseModel {

    private Long id;
    private UUID uuid;
}
