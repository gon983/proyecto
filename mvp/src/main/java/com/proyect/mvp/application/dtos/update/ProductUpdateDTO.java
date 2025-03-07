package com.proyect.mvp.application.dtos.update;




import java.util.UUID;

import lombok.Getter;

@Getter
public class ProductUpdateDTO {
    private UUID idProduct;
    private String name;
    private double stock;
    private double alertStock;
    private String photo;
    private String unitMeasurement;
    private UUID fkProductor;
}