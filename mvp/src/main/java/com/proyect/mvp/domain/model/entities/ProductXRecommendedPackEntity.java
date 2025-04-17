package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.locationtech.jts.geom.Point;

import java.util.UUID;
import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("product_x_recommended_pack")
public class ProductXRecommendedPackEntity {
    @Id
    @Column("id_product_x_recommended_pack")
    private UUID idProductXRecommendedPack;
    @Column("fk_recommended_pack")
    private UUID fkRecommendedPack;
    @Column("fk_product")
    private UUID fkProduct;
    @Column("quantity")
    private Double quantity;
} 