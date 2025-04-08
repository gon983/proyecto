package com.proyect.mvp.application.dtos.response;



import java.time.LocalDateTime;

public record LocationResponseDTO(
    String id,
    String address,
    Double latitude,
    Double longitude

) {}
