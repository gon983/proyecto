package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@AllArgsConstructor
@Table("city")
@Getter
@NoArgsConstructor
public class CityEntity {
    @Id 
    @Column( "id_city")
    private UUID idCity;
    private String name;
    @Column( "fk_country")
    private UUID countryId;
    
    public CityEntity(String name , UUID countryId) {
        this.idCity = UUID.randomUUID();
        this.name = name;
        this.countryId = countryId;
    }
}