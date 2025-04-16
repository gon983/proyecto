package com.proyect.mvp.application.dtos.create;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;

@Getter
public class ProductCreateDTO {
     
    private String name;

    private String photo;

  
    private String unitMeasurement;

   
    private UUID fkCategory;

}
