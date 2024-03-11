package ru.clevertec.house.model.entity.parent;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Getter
@Setter
@FieldNameConstants
@MappedSuperclass
public abstract class UuidModel extends BaseModel {

    @Column(nullable = false, unique = true)
    private UUID uuid;
}
