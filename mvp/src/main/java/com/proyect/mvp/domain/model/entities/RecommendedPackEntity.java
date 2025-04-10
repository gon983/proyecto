
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

import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Transient;
import java.time.LocalDateTime;



@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("recommended_pack")
public class RecommendedPackEntity {
    private UUID idRecommendedPack;
    private String name;
    private String description;
    private String imageUrl;
    @Transient
    private List<ProductEntity> products;
    
    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
} 
    

