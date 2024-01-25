package ru.clevertec.house.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.clevertec.house.listener.HouseListener;
import ru.clevertec.house.model.entity.parent.UuidModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity(name = "house")
@EntityListeners(HouseListener.class)
public class House extends UuidModel {

    private String area;
    private String country;
    private String city;
    private String street;
    private int number;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "home", cascade = CascadeType.ALL)
    private List<Person> residents;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "house_owner",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"),
            foreignKey = @ForeignKey(name = "fk_house_owner_id_to_id")
    )
    private List<Person> owners;
}
