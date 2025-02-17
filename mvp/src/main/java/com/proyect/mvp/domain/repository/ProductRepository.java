package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

    Flux<ProductEntity> findByFkProductor(UUID fkProductor);

    @Query("INSERT INTO product (id_product, name, stock, alert_stock, photo, unit_measurement, fk_productor) " +
            "VALUES (:idProduct, :name, :stock, :alertStock, :photo, :unitMeasurement, :fkProductor)")
    Mono<ProductEntity> insertProduct(UUID idProduct, String name, double stock, double alertStock, String photo, String unitMeasurement, UUID fkProductor);

    @Query("UPDATE product SET name = :name, stock = :stock, alert_stock = :alertStock, photo = :photo, unit_measurement = :unitMeasurement, fk_productor = :fkProductor WHERE id_product = :idProduct")
    Mono<Void> updateProduct(UUID idProduct, String name, double stock, double alertStock, String photo, String unitMeasurement, UUID fkProductor);

    Mono<ProductEntity> findById(UUID id); // Para el método update alternativo
}
