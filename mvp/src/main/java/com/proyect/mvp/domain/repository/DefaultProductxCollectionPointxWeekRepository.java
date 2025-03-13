package com.proyect.mvp.domain.repository;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;

import com.proyect.mvp.domain.model.entities.DefaultProductxCollectionPointxWeekEntity;

import reactor.core.publisher.Flux;

public interface DefaultProductxCollectionPointxWeekRepository extends R2dbcRepository<DefaultProductxCollectionPointxWeekEntity, UUID> {
    
    @Query("SELECT * FROM default_product_x_collection_point_x_week " +
       "WHERE fk_collection_point = :fkCollectionPoint " +
       "AND date_renewal_default_products BETWEEN :startDate AND :endDate")
    Flux<DefaultProductxCollectionPointxWeekEntity> findAllByFkCollectionPointAndDateRange(@Param("fkCollectionPoint") UUID fkCollectionPoint,
                                                                                          @Param("startDate") OffsetDateTime startDate,
                                                                                          @Param("endDate") OffsetDateTime endDate);

    Flux<DefaultProductxCollectionPointxWeekEntity> findAllByFkCollectionPoint(UUID fkCollectionPoint);


    @Query("SELECT * FROM default_product_x_collection_point_x_week " +
       "WHERE fk_collection_point = :fkCollectionPoint " +
       "AND date_renewal_default_products BETWEEN :startDate AND :endDate" +
       " AND fk_product IS NULL")
    Flux<DefaultProductxCollectionPointxWeekEntity> findAllWhereDateIsNearWithFkCollectionPointAndFkProductNull(@Param("fkCollectionPoint") UUID fkCollectionPoint,
                                                                                                                  @Param("startDate") OffsetDateTime startDate,
                                                                                                                  @Param("endDate") OffsetDateTime endDate);

}
