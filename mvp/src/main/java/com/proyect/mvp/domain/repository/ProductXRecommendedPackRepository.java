package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.ProductXRecommendedPackEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductXRecommendedPackRepository extends R2dbcRepository<ProductXRecommendedPackEntity, UUID> {
    @Query("SELECT * FROM product_x_recommended_pack WHERE fk_recommended_pack = :packId")
    Flux<ProductXRecommendedPackEntity> findAllByPackId(UUID packId);

    @Query("DELETE FROM product_x_recommended_pack WHERE fk_recommended_pack = :idPack AND fk_product = :idProduct ")
    Mono<Void> quitarProductoDelPack(@Param("idPack") UUID idPack, @Param("idProduct") UUID idProduct);
}