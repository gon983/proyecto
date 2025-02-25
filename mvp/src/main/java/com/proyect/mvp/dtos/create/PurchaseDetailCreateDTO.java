package com.proyect.mvp.dtos.create;

import java.util.UUID;

import lombok.Getter;


@Getter
public class PurchaseDetailCreateDTO{
    UUID fkProduct;
    double quantity;
    double unitPrice;
}

