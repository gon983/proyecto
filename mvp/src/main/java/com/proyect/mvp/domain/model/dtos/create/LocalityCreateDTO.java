package com.proyect.mvp.domain.model.dtos.create;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class LocalityCreateDTO {
    private String name;
    private UUID cityId;
}
