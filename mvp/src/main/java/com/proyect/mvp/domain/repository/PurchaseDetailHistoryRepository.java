package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.PurchaseDetailHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PurchaseDetailHistoryRepository extends R2dbcRepository<PurchaseDetailHistoryEntity, UUID> {

    @Query("SELECT * FROM purchase_detail_history WHERE id_purchase_detail = :purchaseDetailId ORDER BY initial_date ASC")
    Flux<PurchaseDetailHistoryEntity> findByPurchaseDetailId(UUID purchaseDetailId);

    @Query("INSERT INTO purchase_detail_history (id_purchase_detail_history, id_purchase_detail, id_purchase_detail_state, previous_purchase_detail_state, initial_date, final_date, changed_by) " +
            "VALUES (:idPurchaseDetailHistory, :idPurchaseDetail, :idPurchaseDetailState, :previousPurchaseDetailState, :initialDate, :finalDate)")
    Mono<PurchaseDetailHistoryEntity> insertPurchaseDetailHistory(UUID idPurchaseDetailHistory, UUID idPurchaseDetail, UUID idPurchaseDetailState, UUID previousPurchaseDetailState, LocalDateTime initialDate, LocalDateTime finalDate);
}
