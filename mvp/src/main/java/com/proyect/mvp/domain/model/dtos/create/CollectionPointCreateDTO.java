package com.proyect.mvp.domain.model.dtos.create;

import java.util.UUID;


import org.locationtech.jts.geom.Point;
import lombok.Getter;

@Getter
public class CollectionPointCreateDTO {

 

    private String name;

    private UUID fkNeighborhood;


    private Double usePrice;

    private UUID fkOwner;


    private Point ubication;

    private String description;
    
}
