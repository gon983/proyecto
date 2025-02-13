package com.proyect.mvp.dtos.create; // O la ubicación que hayas elegido para los DTOs

import lombok.Getter;
import lombok.NoArgsConstructor; // Necesario para la creación con new

@Getter
@NoArgsConstructor // Importante: Añade este constructor sin argumentos
public class CountryCreateDTO {
    private String name;
}