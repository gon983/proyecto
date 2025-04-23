package com.proyect.mvp.domain.model.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("recorrido")
public class RecorridoEntity {
    @Column("id_recorrido")
    private UUID idRecorrido;
    private String name;
    private boolean active;
    @Column("cantidad_km")
    private double cantidadKm;
    private ZonedDateTime fecha;
}