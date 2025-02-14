package com.proyect.mvp.domain.repository;

import com.proyect.mvp.domain.model.entities.ProductStateEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductStateRepository extends R2dbcRepository<ProductStateEntity, UUID> {

    @Query("INSERT INTO productstate (id_product_state, name) VALUES (:idProductState, :name)")
    Mono<ProductStateEntity> insertProductState(UUID idProductState, String name);
}
