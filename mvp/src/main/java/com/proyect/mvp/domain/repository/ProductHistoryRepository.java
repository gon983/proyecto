package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.ProductHistoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ProductHistoryRepository extends R2dbcRepository<ProductHistoryEntity, UUID> {

    @Query("SELECT * FROM product_history WHERE fk_product = :productId ORDER BY change_date ASC")
    Flux<ProductHistoryEntity> findByProductId(UUID productId);

    @Query("INSERT INTO product_history (id_product_history, fk_product, fk_product_state, change_date, changed_by) " +
            "VALUES (:idProductHistory, :idProduct, :idProductState, :previousProductState, :changeDate, :changedBy)")
    Mono<ProductHistoryEntity> insertProductHistory(UUID idProductHistory, UUID idProduct, UUID idProductState, UUID previousProductState, LocalDateTime changeDate, UUID changedBy);
}
