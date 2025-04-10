package com.proyect.mvp.application.dtos.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // Add this
@AllArgsConstructor // Add this
public class PurchaseFollowDTO {
    private UUID purchaseId;
    private String status;
    private List<PurchaseDetailDTO> details;
    private LocationResponseDTO location;
    private String estimatedDeliveryTime;
}
