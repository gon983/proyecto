package com.proyect.mvp.application.dtos.update;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecorridoUpdateDTO {
    private UUID idRecorrido;
    private String name;
   
    private double cantidadKm;
}