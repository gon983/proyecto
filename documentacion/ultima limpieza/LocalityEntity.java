package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Table("locality")
@Getter
@NoArgsConstructor
public class LocalityEntity {
    @Id
    @Column("id_locality")
    private UUID idLocality;
    private String name;
    @Column("fk_city")
    private UUID cityId;
    @Transient
    private Set<NeighborhoodEntity> neighborhoods;

    public LocalityEntity(String name, UUID cityId) {
        this.idLocality = UUID.randomUUID();
        this.name = name;
        this.cityId = cityId;
    }

    public void insertNeighborhoods(Set<NeighborhoodEntity> neighborhoods) {
        this.neighborhoods = neighborhoods;
    }
}