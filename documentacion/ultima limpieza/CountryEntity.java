package com.proyect.mvp.domain.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Table("country")
public class CountryEntity {

    @Id
    @Column("id_country")
    private UUID idCountry;

    private String name;
    @Transient
    private Set<CityEntity> cities;

    public void insertCities(Set<CityEntity> cities) {
        this.cities = cities;
    }
   
   

}