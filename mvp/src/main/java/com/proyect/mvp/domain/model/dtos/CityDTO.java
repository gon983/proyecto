package com.proyect.mvp.domain.model.dtos;
import lombok.Getter;
import lombok.NoArgsConstructor; // Necesario para la creación con new
import java.util.UUID;

@Getter
@NoArgsConstructor
public class CityDTO {
    private String name;
    private UUID countryId;
}
