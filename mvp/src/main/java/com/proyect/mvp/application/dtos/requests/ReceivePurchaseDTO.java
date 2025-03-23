package com.proyect.mvp.application.dtos.requests;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivePurchaseDTO {
    List<UUID> detailsReceived;

    public boolean contains(UUID idPurchaseDetail) {
        return detailsReceived.contains(idPurchaseDetail);
    }
    
}
