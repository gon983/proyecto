package com.proyect.mvp.application.dtos.create;

// LocationCreateDTO.java


import java.util.UUID;

public record LocationCreateDTO(
    UUID userId,
    String address,
    Double latitude,
    Double longitude
  
) {}