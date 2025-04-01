package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.CollectionPointCreateDTO;
import com.proyect.mvp.application.dtos.update.CollectionPointUpdateDTO;
import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

import com.proyect.mvp.infrastructure.config.converters.SpatialConverter;

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
   
    private final GeometryFactory geometryFactory;
    private final SpatialConverter spatialConverter;

    public CollectionPointService(CollectionPointRepository collectionPointRepository,
                                GeometryFactory geometryFactory, SpatialConverter spatialConverter) {
        this.collectionPointRepository = collectionPointRepository;
        
        this.geometryFactory = geometryFactory;
        this.spatialConverter = spatialConverter;
        
    }

    public Flux<CollectionPointEntity> getAllCollectionPoints() {
        return collectionPointRepository.findAll();
    }
    

    public Mono<CollectionPointEntity> getCollectionPointById(UUID id) {
        return collectionPointRepository.findById(id);
    }

    public Mono<CollectionPointEntity> getCollectionPointByFkNeighborhood(UUID idNeighborhood){
        return collectionPointRepository.getCollectionPointByFkNeighborhood(idNeighborhood);
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
