package com.proyect.mvp.domain.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table("city")
@Getter
@NoArgsConstructor
public class CityEntity {
    @Id 
    @Column( "id_city")
    private String idCity;
    private String name;
    private CountryEntity country; 
}