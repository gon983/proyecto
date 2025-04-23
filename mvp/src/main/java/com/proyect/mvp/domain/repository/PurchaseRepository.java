package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.application.dtos.database.PurchaseToFollowDTO;

import com.proyect.mvp.domain.model.entities.PurchaseEntity;

import reactor.core.publisher.Flux;
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

    @Query("UPDATE purchase SET fk_recorrido = :idRecorrido WHERE id_purchase = :idPurchase  ")
    Mono<Void> updateRecorrido(@Param("idPurchase") UUID idPurchase,@Param("idRecorrido") UUID idRecorrido);

    @Query("SELECT * FROM purchase WHERE fk_user = :idUser AND fk_current_state <> :idState ORDER BY mp_payment_date LIMIT 5")
    Flux<PurchaseToFollowDTO> findLastFiveUserNotPending(@Param("idUser") UUID idUser, @Param("idState") UUID idState);

    @Query("SELECT * FROM purchase WHERE fk_current_state = :fk_current_state")
    Flux<PurchaseEntity> findByFkCurrentState(@Param("fk_current_state") UUID fkCurrentState);

}
