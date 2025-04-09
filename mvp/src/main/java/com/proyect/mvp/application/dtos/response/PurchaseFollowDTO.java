package com.proyect.mvp.application.dtos.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class PurchaseFollowDTO {
    private UUID purchaseId;
    private String status; // "confirmed", "elaborating", "delivering", "near_you", "delivered"
   
    private List<PurchaseDetailDTO> details;
    private LocationResponseDTO location;
    private String estimatedDeliveryTime; // Tiempo estimado para entrega
    private UUID deliveryPersonId; // ID de la persona que hace la entrega (si aplica)
    private String deliveryPersonName; // Nombre de quien entrega (si aplica)
    
    // Getters y setters
}
