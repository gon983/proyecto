package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table("collectionpointhistory")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CollectionPointHistoryEntity {

    @Id
    @Column("id_collection_point_history")
    private UUID idCollectionPointHistory;  // ID como UUID

    @Column("id_collection_point") // Clave foránea a CollectionPoint
    private UUID fkCollectionPoint; // Usar UUID y nombre consistente

    @Column("id_collection_point_state") // Clave foránea a CollectionPointState
    private UUID fkCollectionPointState; // Usar UUID y nombre consistente

    @Column("description")
    private String description;

    @Column("initial_date")
    private LocalDateTime init;

    @Column("final_date")
    private LocalDateTime finish;
}