package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Table("neighborhood_package_state")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageStateEntity {

    @Id
    @Column("id_neighborhood_package_state")
    private UUID idNeighborhoodPackageState;

    @Column("name")
    private String name;

    public NeighborhoodPackageStateEntity(String name) {
        this.idNeighborhoodPackageState = UUID.randomUUID();
        this.name = name;
    }
}