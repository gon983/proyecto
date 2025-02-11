package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "locality")
@Getter
@NoArgsConstructor
public class LocalityEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_locality")
    private String idLocality;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_city")
    private CityEntity  city; // Assuming you have a City entity
}