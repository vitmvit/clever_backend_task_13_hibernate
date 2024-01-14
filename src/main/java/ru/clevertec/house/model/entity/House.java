package ru.clevertec.house.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import ru.clevertec.house.model.entity.parent.BaseModel;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class House extends BaseModel {

    private double area;
    private String country;
    private String city;
    private String street;
    private int number;
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "home", cascade = CascadeType.PERSIST)
    private List<Person> residents;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "house_owner",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"),
            foreignKey = @ForeignKey(name = "fk_house_owner_id_to_id")
    )
    private List<Person> owners;
}
