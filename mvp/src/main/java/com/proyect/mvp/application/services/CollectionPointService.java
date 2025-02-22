package com.proyect.mvp.application.services;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;
import com.proyect.mvp.domain.model.entities.CollectionPointHistoryEntity;
import com.proyect.mvp.dtos.create.CollectionPointCreateDTO;
import com.proyect.mvp.dtos.update.CollectionPointUpdateDTO;
import com.proyect.mvp.infrastructure.config.converters.SpatialConverter;
import com.proyect.mvp.domain.repository.CollectionPointHistoryRepository;
import com.proyect.mvp.domain.repository.CollectionPointRepository;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;
    private final CollectionPointHistoryService collectionPointHistoryService;
    private final GeometryFactory geometryFactory;
    private final SpatialConverter spatialConverter;

    public CollectionPointService(CollectionPointRepository collectionPointRepository,
                                CollectionPointHistoryService collectionPointHistoryService, GeometryFactory geometryFactory, SpatialConverter spatialConverter) {
        this.collectionPointRepository = collectionPointRepository;
        this.collectionPointHistoryService = collectionPointHistoryService;
        this.geometryFactory = geometryFactory;
        this.spatialConverter = spatialConverter;
        
    }

    public Flux<CollectionPointEntity> getAllCollectionPoints() {
        return collectionPointRepository.findAll()
                .flatMap(collectionPoint -> {
                    return collectionPointHistoryService.getCollectionPointHistory(collectionPoint.getIdCollectionPoint())
                            .collectList()
                            .flatMap(historyList -> {
                                collectionPoint.addHistory(historyList);
                                return Mono.just(collectionPoint);
                            });
                });
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
            // Create JTS Point object
            Double latitude = collectionPointDTO.getUbication().getY();
            Double longitude = collectionPointDTO.getUbication().getX();
            Coordinate coordinate = new Coordinate(longitude, latitude);
            Point ubication = geometryFactory.createPoint(coordinate);
            ubication.setSRID(4326);
            byte[] wkbUbication = spatialConverter.pointToWKB(ubication);

            return collectionPointRepository.saveNew(
                UUID.randomUUID(),
                collectionPointDTO.getName(),
                collectionPointDTO.getFkNeighborhood(),
                collectionPointDTO.getUsePrice(),
                collectionPointDTO.getFkOwner(),
                wkbUbication,
                collectionPointDTO.getDescription()
            );
        }

    public Mono<CollectionPointEntity> updateCollectionPoint(UUID id, CollectionPointUpdateDTO collectionPointDTO) {
        return collectionPointRepository.findById(id)
                .flatMap(existingCollectionPoint -> {
                    CollectionPointEntity updatedCollectionPoint = CollectionPointEntity.builder()
                            .idCollectionPoint(id)
                            .name(collectionPointDTO.getName())
                            .fkNeighborhood(collectionPointDTO.getFkNeighborhood())
                            .usePrice(collectionPointDTO.getUsePrice())
                            .fkOwner(collectionPointDTO.getFkOwner())
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
