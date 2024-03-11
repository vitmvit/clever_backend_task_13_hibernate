package ru.clevertec.house.model.entity.parent;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class LogModel extends UuidModel {

    @CreationTimestamp
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;
}
