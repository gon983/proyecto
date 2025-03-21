package com.proyect.mvp.application.dtos.response;

import java.util.List;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;


@AllArgsConstructor
@Builder
public class CollectionPointSalesDTO {
    CollectionPointEntity collectionPoint;
    List<SaleSummaryDTO> sales;
    
    
}
