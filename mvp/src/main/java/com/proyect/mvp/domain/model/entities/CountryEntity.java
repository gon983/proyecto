package com.proyect.mvp.domain.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@Table("country")
public class CountryEntity {

    @Id
    @Column("id_country")
    private UUID idCountry;

    private String name;

    public CountryEntity() {
        this.idCountry = UUID.randomUUID();
    }

    public CountryEntity(String name) {
        this.idCountry = UUID.randomUUID();
        this.name = name;
    }

}