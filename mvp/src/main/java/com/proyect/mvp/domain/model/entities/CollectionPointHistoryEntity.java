package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime; // Importa LocalDateTime para los tipos de datos datetime


@Table( "collectionpointhistory")
@Getter
@NoArgsConstructor
public class CollectionPointHistoryEntity { // PascalCase

    @Id
    
    @Column( "id_collection_point_history")
    private String idCollectionPointHistory;

    
    
    private CollectionPointEntity collectionPoint; // Relación con CollectionPoint

    
    
    private CollectionPointStateEntity  collectionPointState; // Relación con CollectionPointState

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init; // Usando LocalDateTime para datetime

    @Column( "finish")
    private LocalDateTime finish; // Usando LocalDateTime para datetime
}

