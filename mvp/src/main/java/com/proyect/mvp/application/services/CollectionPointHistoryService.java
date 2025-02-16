package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;
import com.proyect.mvp.domain.repository.CollectionPointHistoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CollectionPointHistoryService {

    private final CollectionPointHistoryRepository collectionPointHistoryRepository;

    public CollectionPointHistoryService(CollectionPointHistoryRepository collectionPointHistoryRepository) {
        this.collectionPointHistoryRepository = collectionPointHistoryRepository;
    }

    public Flux<CollectionPointHistoryEntity> getCollectionPointHistory(UUID collectionPointId) {
        return collectionPointHistoryRepository.findByCollectionPointId(collectionPointId);
    }

    public Mono<CollectionPointHistoryEntity> registerCollectionPointStateChange(UUID collectionPointId, UUID collectionPointStateId, String description, LocalDateTime init, LocalDateTime finish) {
        UUID idCollectionPointHistory = UUID.randomUUID();
        return collectionPointHistoryRepository.insertCollectionPointHistory(idCollectionPointHistory, collectionPointId, collectionPointStateId, description, init, finish);
    }
}