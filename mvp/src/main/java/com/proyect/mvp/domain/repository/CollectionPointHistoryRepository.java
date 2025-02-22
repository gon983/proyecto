package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface CollectionPointHistoryRepository extends R2dbcRepository<CollectionPointHistoryEntity, UUID> {

    @Query("SELECT * FROM collection_point_history WHERE id_collection_point = :collectionPointId")
    Flux<CollectionPointHistoryEntity> findByCollectionPointId(UUID collectionPointId);

    @Query("INSERT INTO collection_point_history (id_collection_point_history, id_collection_point, id_collection_point_state, description, initial_date, final_date) " +
           "VALUES (:idCollectionPointHistory, :idCollectionPoint, :idCollectionPointState, :description, :initialDate, :finalDate)")
    Mono<CollectionPointHistoryEntity> insertCollectionPointHistory(UUID idCollectionPointHistory, UUID idCollectionPoint, UUID idCollectionPointState, String description, LocalDateTime initialDate, LocalDateTime finalDate);
}
