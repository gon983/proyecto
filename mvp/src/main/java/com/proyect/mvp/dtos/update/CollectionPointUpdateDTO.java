package com.proyect.mvp.dtos.update;



import java.util.UUID;
import org.locationtech.jts.geom.Point;

import lombok.Getter;

@Getter
public class CollectionPointUpdateDTO {
    private String name;
    private UUID fk_neighborhood;
    private Double usePrice;
    private UUID fk_owner;
    private Point ubication;
    private String description;

    
}