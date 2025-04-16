package com.proyect.mvp.application.dtos.update;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;


@Getter
public class ProductContaduryUpdateDTO {
    private UUID idProduct;
    private Double unity_price;
    
    private Double unityCost;
    
    private Double cantidadVendidaSemana;
    
    private Double dineroGeneradoSemana;
    
}
