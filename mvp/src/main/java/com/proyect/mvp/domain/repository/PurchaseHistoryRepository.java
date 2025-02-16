package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.PurchaseHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PurchaseHistoryRepository extends R2dbcRepository<PurchaseHistoryEntity, UUID> {

    @Query("SELECT * FROM purchase_history WHERE id_purchase = :purchaseId ORDER BY initial_date ASC")
    Flux<PurchaseHistoryEntity> findByPurchaseId(UUID purchaseId);

    @Query("INSERT INTO purchase_history (id_purchase_history, id_purchase, id_purchase_state, previous_purchase_state, initial_date, final_date, changed_by) " +
            "VALUES (:idPurchaseHistory, :idPurchase, :idPurchaseState, :previousPurchaseState, :initialDate, :finalDate, :changedBy)")
    Mono<PurchaseHistoryEntity> insertPurchaseHistory(UUID idPurchaseHistory, UUID idPurchase, UUID idPurchaseState, UUID previousPurchaseState, LocalDateTime initialDate, LocalDateTime finalDate, UUID changedBy);
}
