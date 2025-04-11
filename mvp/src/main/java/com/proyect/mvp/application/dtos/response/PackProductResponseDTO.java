package com.proyect.mvp.application.dtos.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class PackProductResponseDTO {
    private UUID productId;
    private String name;
    private Double quantity;
    private Double price;
    
}
