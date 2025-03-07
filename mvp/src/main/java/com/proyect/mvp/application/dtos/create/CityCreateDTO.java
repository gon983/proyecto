package com.proyect.mvp.application.dtos.create;
import lombok.Getter;
import lombok.NoArgsConstructor; // Necesario para la creación con new
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CityCreateDTO {
    private String name;
    private UUID countryId;
}
