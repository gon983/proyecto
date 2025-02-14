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
@Table("collectionpointstate")
@Getter
@NoArgsConstructor
public class CollectionPointStateEntity {

    @Id
    @Column("id_collection_point_state")
    private UUID idCollectionPointState;

    @Column("name")
    private String name;

    public CollectionPointStateEntity(String name) {
        this.idCollectionPointState = UUID.randomUUID();
        this.name = name;
    }
}
