package com.proyect.mvp.application.dtos.create;

import java.util.UUID;

import lombok.Getter;

@Getter
public class StandarProductCreateDTO {
    
    private String name;
    private UUID fkCategory;

    
    
}
