package com.proyect.mvp.application.dtos.requests;

import java.util.List;
import java.util.UUID;

public class ReceivePurchaseDTO {
    List<UUID> detailsReceived;

    public boolean contains(String idPurchaseDetail) {
        return detailsReceived.contains(idPurchaseDetail);
    }
    
}
