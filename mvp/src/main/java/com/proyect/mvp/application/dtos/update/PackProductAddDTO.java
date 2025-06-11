package com.proyect.mvp.application.dtos.update;

import java.util.UUID;

import lombok.Getter;

@Getter
public class PackProductAddDTO {
    private UUID idPack;
    private UUID productId;
    private Double quantity;

    
}
