package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;
import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;
import com.proyect.mvp.dtos.create.CollectionPointCreateDTO;
import com.proyect.mvp.dtos.update.CollectionPointUpdateDTO;
import com.proyect.mvp.domain.repository.CollectionPointHistoryRepository;
import com.proyect.mvp.domain.repository.CollectionPointRepository;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Service
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;
    private final CollectionPointHistoryService collectionPointHistoryService;

    public CollectionPointService(CollectionPointRepository collectionPointRepository,
                                CollectionPointHistoryService collectionPointHistoryService) {
        this.collectionPointRepository = collectionPointRepository;
        this.collectionPointHistoryService = collectionPointHistoryService;
        
    }

    public Flux<CollectionPointEntity> getAllCollectionPoints() {
        return collectionPointRepository.findAll();
    }
    

    public Mono<CollectionPointEntity> getCollectionPointById(UUID id) {
        return collectionPointRepository.findById(id)
                .flatMap(existingCollectionPoint -> {
                    return collectionPointHistoryService.getCollectionPointHistory(id)
                            .collectList()
                            .flatMap(historyList -> {
                                existingCollectionPoint.addHistory(historyList); // Asumiendo que addHistory ahora acepta List
                                return Mono.just(existingCollectionPoint);
                            });
                });
    }

    public Mono<CollectionPointEntity> saveNewCollectionPoint(CollectionPointCreateDTO collectionPointDTO) {
        CollectionPointEntity collectionPoint = CollectionPointEntity.builder()
                .idCollectionPoint(UUID.randomUUID())
                .name(collectionPointDTO.getName())
                .fk_neighborhood(collectionPointDTO.getFk_neighborhood())
                .usePrice(collectionPointDTO.getUsePrice())
                .fk_owner(collectionPointDTO.getFk_owner())
                .ubication(collectionPointDTO.getUbication())
                .description(collectionPointDTO.getDescription())
                .build();
        return collectionPointRepository.save(collectionPoint);
    }

    public Mono<CollectionPointEntity> updateCollectionPoint(UUID id, CollectionPointUpdateDTO collectionPointDTO) {
        return collectionPointRepository.findById(id)
                .flatMap(existingCollectionPoint -> {
                    CollectionPointEntity updatedCollectionPoint = CollectionPointEntity.builder()
                            .idCollectionPoint(id)
                            .name(collectionPointDTO.getName())
                            .fk_neighborhood(collectionPointDTO.getFk_neighborhood())
                            .usePrice(collectionPointDTO.getUsePrice())
                            .fk_owner(collectionPointDTO.getFk_owner())
                            .ubication(collectionPointDTO.getUbication())
                            .description(collectionPointDTO.getDescription())
                            .build();
                    return collectionPointRepository.save(updatedCollectionPoint);
                });
    }

    public Mono<Void> deleteCollectionPointById(UUID id) {
        return collectionPointRepository.deleteById(id);
    }
}
