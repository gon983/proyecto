package com.proyect.mvp.application.dtos.response;

import java.util.UUID;

import com.proyect.mvp.domain.model.entities.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SaleSummaryDTO {
    private UUID idProduct;
    private String name;
    private double stock;
    private String unitMeasurement;
    double totalQuantity;
    double totalAmount;

    
        
}