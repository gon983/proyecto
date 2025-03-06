package com.proyect.mvp.domain.model.dtos.create;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.domain.model.entities.StockMovementTypeEnum;

import lombok.Getter;

@Getter
public class StockMovementCreateDTO {
    
    
    private UUID fkProduct;
 
    private double quantity;

    private StockMovementTypeEnum  type; 

    private String comment; 
    
}
