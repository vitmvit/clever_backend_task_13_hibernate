package ru.clevertec.house.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.constant.Sex;
import ru.clevertec.house.listener.PersonListener;
import ru.clevertec.house.model.entity.parent.LogModel;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity(name = "person")
@EntityListeners(PersonListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"uuid", "passport_series", "passport_number"}))
public class Person extends LogModel {

    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Embedded
    private Passport passport;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "house_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_home_resident_id_to_id")
    )
    private House home;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "house_owner",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "house_id"),
            foreignKey = @ForeignKey(name = "fk_owner_house_id_to_id")
    )
    private List<House> houses;
}
