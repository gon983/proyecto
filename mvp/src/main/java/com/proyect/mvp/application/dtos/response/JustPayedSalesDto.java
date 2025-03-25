package com.proyect.mvp.application.dtos.response;

import java.util.List;
import java.util.UUID;

import com.proyect.mvp.domain.model.entities.SaleEntity;

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
public class JustPayedSalesDto {
    UUID idProduct;
    List<SaleEntity> sales;
    
}
