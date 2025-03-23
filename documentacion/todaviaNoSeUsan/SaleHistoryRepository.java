package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.SaleHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SaleHistoryRepository extends R2dbcRepository<SaleHistoryEntity, UUID> {

    @Query("SELECT * FROM sale_history WHERE id_sale = :saleId ORDER BY initial_date ASC")
    Flux<SaleHistoryEntity> findBySaleId(UUID saleId);

    @Query("INSERT INTO sale_history (id_sale_history, fk_sale, fk_sale_state,  initial_date, final_date, changed_by) " +
            "VALUES (:idSaleHistory, :idSale, :idSaleState, :initialDate, :finalDate)")
    Mono<SaleHistoryEntity> insertSaleHistory(UUID idSaleHistory, UUID idSale, UUID idSaleState, LocalDateTime initialDate, LocalDateTime finalDate);
}