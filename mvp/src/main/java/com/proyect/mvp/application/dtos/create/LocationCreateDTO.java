package com.proyect.mvp.application.dtos.create;

// LocationCreateDTO.java


import java.util.UUID;

public record LocationCreateDTO(
    UUID userId,
    String address,
    UUID neighborhoodId,
    Double latitude,
    Double longitude,
    UUID localityId
) {}