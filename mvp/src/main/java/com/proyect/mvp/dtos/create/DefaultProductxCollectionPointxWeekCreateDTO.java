package com.proyect.mvp.dtos.create;

import java.util.UUID;

import org.springframework.data.relational.core.mapping.Column;

import lombok.Getter;

@Getter
public class DefaultProductxCollectionPointxWeekCreateDTO {
    UUID fkCollectionPoint;
    UUID fkProduct;
    UUID fkStandarProduct;
}
