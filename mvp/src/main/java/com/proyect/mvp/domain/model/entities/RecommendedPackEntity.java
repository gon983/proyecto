
package com.proyect.mvp.domain.model.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.proyect.mvp.application.dtos.create.PackProductDTO;
import com.proyect.mvp.application.dtos.response.PackProductResponseDTO;

import org.springframework.data.relational.core.mapping.Column;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Transient;
import java.time.LocalDateTime;



@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("recommended_pack")
public class RecommendedPackEntity {
    @Id
    @Column("id_recommended_pack")
    private UUID idRecommendedPack;
    private String name;
    private String description;
    @Column("image_url")
    private String imageUrl;
    @Transient
    private List<PackProductResponseDTO> products;
    
    public void setProducts(List<PackProductResponseDTO> products) {
        this.products = products;
    }

    
} 
    

