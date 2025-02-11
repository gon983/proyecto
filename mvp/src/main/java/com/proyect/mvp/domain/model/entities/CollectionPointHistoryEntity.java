package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime; // Importa LocalDateTime para los tipos de datos datetime

@Entity
@Table(name = "collectionpointhistory")
@Getter
@NoArgsConstructor
public class CollectionPointHistoryEntity { // PascalCase

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_collection_point_history")
    private String idCollectionPointHistory;

    @ManyToOne
    @JoinColumn(name = "fk_collection_point")
    private CollectionPoint collectionPoint; // Relación con CollectionPoint

    @ManyToOne
    @JoinColumn(name = "fk_collection_point_state")
    private CollectionPointState collectionPointState; // Relación con CollectionPointState

    @Column(name = "description")
    private String description;

    @Column(name = "init")
    private LocalDateTime init; // Usando LocalDateTime para datetime

    @Column(name = "finish")
    private LocalDateTime finish; // Usando LocalDateTime para datetime
}
