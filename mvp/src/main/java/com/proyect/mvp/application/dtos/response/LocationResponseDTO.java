package com.proyect.mvp.application.dtos.response;



import java.time.LocalDateTime;

public record LocationResponseDTO(
    String id,
    String address,
    String neighborhood,
    Double latitude,
    Double longitude,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
