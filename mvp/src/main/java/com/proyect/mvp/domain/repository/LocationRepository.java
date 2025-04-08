package com.proyect.mvp.domain.repository;



import com.proyect.mvp.domain.model.entities.Location;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface LocationRepository extends ReactiveCrudRepository<Location, UUID> {

    Flux<Location> findByUserId(UUID userId);
}
