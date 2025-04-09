package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.Location;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.List;


public interface LocationRepository extends ReactiveCrudRepository<Location, UUID> {

    Flux<Location> findByUserId(UUID userId);

    Mono<Location> findById(UUID id);

    @Query("UPDATE LOCATIONS SET active = false WHERE id = :idLocation ")
    Mono<Void> deleteLogical(@Param("idLocation") UUID idLocation);
}
