package com.proyect.mvp.dtos.create;

import java.util.UUID;


import org.locationtech.jts.geom.Point;
import lombok.Getter;

@Getter
public class CollectionPointCreateDTO {

 

    private String name;

    private UUID fk_neighborhood;


    private Double usePrice;

    private UUID fk_owner;


    private Point ubication;

    private String description;
    
}
