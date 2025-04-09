package com.proyect.mvp.application.dtos.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseDetailDTO {
    private UUID detailId;
    private String productName;
    private double quantity;
    private double unitPrice;
    private String productImageUrl;
    
    // Getters y setters
}
