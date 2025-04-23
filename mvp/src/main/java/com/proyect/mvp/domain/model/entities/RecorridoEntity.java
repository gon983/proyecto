package com.proyect.mvp.domain.model.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecorridoEntity {
    private UUID idRecorrido;
    private String name;
    private boolean active;
    private double cantidadKm;
    private ZonedDateTime fecha;
}