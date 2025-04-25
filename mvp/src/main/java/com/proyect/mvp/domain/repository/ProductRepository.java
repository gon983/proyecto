package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {



    @Query("UPDATE product SET stock = $2 WHERE id_product = $1")
    Mono<Void> updateStock(UUID idProduct, double newStock); 

    Mono<ProductEntity> findById(UUID id); // Para el método update alternativo


    @Query("SELECT * FROM product WHERE active = true and LOWER(name) LIKE LOWER('%' || :name || '%')")
    Flux<ProductEntity> findAllFilterByName(@Param("name") String name);

    @Query("SELECT * FROM product WHERE fk_category = :id_category AND active = true")
    Flux<ProductEntity> findAllFilterByCategory(@Param("id_category") UUID id_category);
}
