package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;

import reactor.core.publisher.Mono;

import java.util.List;


public interface PurchaseRepository extends R2dbcRepository<PurchaseEntity,UUID> {


    @Query("Select * from purchase where mp_preference_id = :mpPreferenceId")
    Mono<PurchaseEntity> findByMpPreferenceId(@Param("mpPreferenceId")String mpPreferenceId);

    Mono<PurchaseEntity> findByIdPurchase(UUID idPurchase);

    @Query("SELECT * from purchase where fk_user = :fkUser AND fk_current_state = :fkCurrentState")
    Mono<PurchaseEntity> findByFkUserAndFkCurrentState(@Param("fkUser") UUID fkUser,@Param("fkCurrentState") UUID fkCurrentState);

    @Query("UPDATE purchase SET id_location = :idLocation WHERE id_purchase = :idPurchase  ")
    Mono<Void> updateLocation(@Param("idPurchase") UUID idPurchase,@Param("idLocation") UUID idLocation);

}
