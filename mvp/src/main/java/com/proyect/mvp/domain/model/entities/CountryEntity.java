package com.proyect.mvp.domain.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@Table("country")
public class CountryEntity {

    @Id
    @Column("id_country")
    private UUID idCountry;

    private String name;

    private Set<CityEntity> cities;

    

    public CountryEntity(String name) {
        this.idCountry = UUID.randomUUID();
        this.name = name;
    }

   

}