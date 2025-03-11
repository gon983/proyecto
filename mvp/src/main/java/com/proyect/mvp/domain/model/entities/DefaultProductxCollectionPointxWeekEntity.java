package com.proyect.mvp.domain.model.entities;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Table("default_product_x_collection_point_x_week")
public class DefaultProductxCollectionPointxWeekEntity {
    @Id
    @Column("id_default_product_x_collection_point")
    UUID idDefaultProductxCollectionPoint;
    @Column("fk_collection_point")
    UUID fkCollectionPoint;
    @Column("fk_product")
    UUID fkProduct;
    @Column("fk_standar_product")
    UUID fkStandarProduct;
    @Column("date_init_week")
    OffsetDateTime dateInitWeek;
    
}
