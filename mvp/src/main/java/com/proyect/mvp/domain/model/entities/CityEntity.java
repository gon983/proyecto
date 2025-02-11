package com.proyect.mvp.domain.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;


@Entity
@Table(name = "city")
@Getter
@NoArgsConstructor
public class CityEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_city")
    private String idCity;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_country")
    private CountryEntity country; 
}