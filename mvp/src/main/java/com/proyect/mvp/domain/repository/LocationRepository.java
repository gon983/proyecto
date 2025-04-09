package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.Location;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.List;


public interface LocationRepository extends ReactiveCrudRepository<Location, UUID> {

    Flux<Location> findByUserId(UUID userId);

    Mono<Location> findById(UUID id);
}
