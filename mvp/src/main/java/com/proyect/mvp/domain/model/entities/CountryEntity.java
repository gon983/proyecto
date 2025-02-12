package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "country")
@Getter
@Setter
@NoArgsConstructor
public class CountryEntity{

    @Id  
    @Column( "id_country")
    private UUID idCountry;

    @Column( "name")
    private String name;


    public CountryEntity(String name) {
        this.idCountry = UUID.randomUUID();
        this.name = name;
    }

}