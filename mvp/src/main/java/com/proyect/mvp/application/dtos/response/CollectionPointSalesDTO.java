package com.proyect.mvp.application.dtos.response;

import java.util.List;

import com.proyect.mvp.domain.model.entities.CollectionPointEntity;

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
public class CollectionPointSalesDTO {
    CollectionPointEntity collectionPoint;
    List<SaleSummaryDTO> sales;
    
    
}
