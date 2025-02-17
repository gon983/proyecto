package com.proyect.mvp.dtos.create;

import java.util.UUID;


import lombok.Getter;

@Getter
public class ProductCreateDTO {
    private String name;
    private double stock;
    private double alertStock;
    private String photo;
    private String unitMeasurement;
    private UUID fkProductor;
}
