package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.locationtech.jts.geom.Point;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

import reactor.core.publisher.Mono;

public interface CollectionPointRepository extends R2dbcRepository<CollectionPointEntity, UUID> {

    @Query("INSERT INTO collection_point " +
            "(id_collection_point, name, fk_neighborhood, use_price, fk_owner, ubication, description) " +
            "VALUES " +
            "(:id_collection_point, :name, :fk_neighborhood, :use_price, :fk_owner, " +
            "ST_SetSRID(ST_GeomFromWKB(:ubication), 4326), :description)") // Use ST_SetSRID and WKB
    Mono<CollectionPointEntity> saveNew(
            @Param("id_collection_point") UUID idCollectionPoint,
            @Param("name") String name,
            @Param("fk_neighborhood") UUID fkNeighborhood,
            @Param("use_price") Double usePrice,
            @Param("fk_owner") UUID fkOwner,
            @Param("ubication") byte[] ubication, // Store as WKB
            @Param("description") String description
    );

    @Query("SELECT * from collection_point  WHERE fk_neighborhood = :idNeigborhood ")
    Mono<CollectionPointEntity> getCollectionPointByFkNeighborhood(@Param("idNeighborhood") UUID idNeighborhood);
}
