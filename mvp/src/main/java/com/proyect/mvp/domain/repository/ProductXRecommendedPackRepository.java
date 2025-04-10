package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.ProductXRecommendedPackEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.util.UUID;

@Repository
public interface ProductXRecommendedPackRepository extends R2dbcRepository<ProductXRecommendedPackEntity, UUID> {
    @Query("SELECT * FROM product_x_recommended_pack WHERE fk_recommended_pack = :packId")
    Flux<ProductXRecommendedPackEntity> findAllByPackId(UUID packId);
}