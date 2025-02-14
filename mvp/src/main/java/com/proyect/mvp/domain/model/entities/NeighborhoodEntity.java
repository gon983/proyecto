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
@Table("neighborhood")
@Getter
@NoArgsConstructor
public class NeighborhoodEntity {
    @Id
    @Column("id_neighborhood")
    private UUID idNeighborhood;
    private String name;
    @Column("fk_locality")
    private UUID localityId;

    public NeighborhoodEntity(String name, UUID localityId) {
        this.idNeighborhood = UUID.randomUUID();
        this.name = name;
        this.localityId = localityId;
    }
}
