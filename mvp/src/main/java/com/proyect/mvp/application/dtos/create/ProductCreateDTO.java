package com.proyect.mvp.application.dtos.create;

import java.util.UUID;


import lombok.Getter;

@Getter
public class ProductCreateDTO {
    private String name;
    private double stock;
    private String photo;
    private String unitMeasurement;
   
    private double unity_price;
   
    private UUID fkCategory;
}
