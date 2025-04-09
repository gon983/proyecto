package com.proyect.mvp.application.dtos.database;

import java.util.UUID;

import lombok.Data;

@Data
public class PurchaseToFollowDTO {
    UUID idPurchase;
    UUID idLocation;
}
