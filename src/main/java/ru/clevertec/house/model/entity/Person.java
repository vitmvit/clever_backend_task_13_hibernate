package ru.clevertec.house.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.constant.SexType;
import ru.clevertec.house.model.entity.parent.BaseModel;

import java.time.LocalDateTime;
import java.util.List;

@Entity
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "house_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_home_resident_id_to_id")
    )
    private House home;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "house_owner",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"),
            foreignKey = @ForeignKey(name = "fk_owner_house_id_to_id")
    )
    private List<House> house;
}
