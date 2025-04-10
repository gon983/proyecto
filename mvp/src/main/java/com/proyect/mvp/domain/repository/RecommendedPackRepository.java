package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.RecommendedPackEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface RecommendedPackRepository extends R2dbcRepository<RecommendedPackEntity, UUID> {
    Flux<RecommendedPackEntity> findAll();
    Mono<RecommendedPackEntity> findByIdRecommendedPack(UUID id);
}