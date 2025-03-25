package com.proyect.mvp.application.dtos.create;

import java.util.UUID;

import lombok.Getter;


@Getter
public class PurchaseDetailCreateDTO{
    UUID fkProduct;
    double quantity;
    double unitPrice;
    UUID fkProductor;
    UUID fkBuyer;
    UUID fkCollectionPoint;
}

