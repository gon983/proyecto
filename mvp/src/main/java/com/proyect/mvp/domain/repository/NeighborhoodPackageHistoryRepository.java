package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface NeighborhoodPackageHistoryRepository extends R2dbcRepository<NeighborhoodPackageHistoryEntity, UUID> {

    @Query("SELECT * FROM neighborhood_package_history WHERE fk_neighborhood_package = :neighborhoodPackageId")
    Flux<NeighborhoodPackageHistoryEntity> findByNeighborhoodPackageId(UUID neighborhoodPackageId);

    @Query("INSERT INTO neighborhood_package_history (id_neighborhood_package_history, fk_neighborhood_package, fk_package_state, description, initial_date, final_date) " +
            "VALUES (:idNeighborhoodPackageHistory, :idNeighborhoodPackage, :idPackageState, :description, :initialDate, :finalDate)")
    Mono<NeighborhoodPackageHistoryEntity> insertNeighborhoodPackageHistory(UUID idNeighborhoodPackageHistory, UUID idNeighborhoodPackage, UUID idPackageState, String description, LocalDateTime initialDate, LocalDateTime finalDate);
}