package com.proyect.mvp.domain.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

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

    // ... getters y setters (necesarios para que R2DBC persista los datos)
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public UUID getIdCountry() {
        return idCountry;
    }
    public void setIdCountry(UUID idCountry) {
        this.idCountry = idCountry;
    }
}