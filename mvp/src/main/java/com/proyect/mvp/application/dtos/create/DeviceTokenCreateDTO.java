package com.proyect.mvp.application.dtos.create;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenCreateDTO {
    private UUID userId;
    private String deviceToken;
    private String deviceType; // "ANDROID", "IOS", "WEB"
} 
