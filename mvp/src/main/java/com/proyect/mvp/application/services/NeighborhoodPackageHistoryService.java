package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.NeighborhoodPackageHistoryEntity;
import com.proyect.mvp.domain.repository.NeighborhoodPackageHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NeighborhoodPackageHistoryService {

    private final NeighborhoodPackageHistoryRepository neighborhoodPackageHistoryRepository;

    public NeighborhoodPackageHistoryService(NeighborhoodPackageHistoryRepository neighborhoodPackageHistoryRepository) {
        this.neighborhoodPackageHistoryRepository = neighborhoodPackageHistoryRepository;
    }

    public Flux<NeighborhoodPackageHistoryEntity> getNeighborhoodPackageHistory(UUID neighborhoodPackageId) {
        return neighborhoodPackageHistoryRepository.findByNeighborhoodPackageId(neighborhoodPackageId);
    }

    public Mono<NeighborhoodPackageHistoryEntity> registerNeighborhoodPackageStateChange(UUID neighborhoodPackageId, UUID packageStateId, String description, LocalDateTime init, LocalDateTime finish) {
        UUID idNeighborhoodPackageHistory = UUID.randomUUID(); // Generate UUID here
        return neighborhoodPackageHistoryRepository.insertNeighborhoodPackageHistory(idNeighborhoodPackageHistory, neighborhoodPackageId, packageStateId, description, init, finish);
    }
}
