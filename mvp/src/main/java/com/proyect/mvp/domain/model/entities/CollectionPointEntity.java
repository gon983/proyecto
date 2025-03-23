package com.proyect.mvp.domain.model.entities;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.locationtech.jts.geom.Point;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@Table( "collection_point")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionPointEntity {

    @Id
    
    @Column( "id_collection_point")
    private UUID idCollectionPoint;

    @Column( "name")
    private String name;
    @Column( "fk_neighborhood")
    private UUID fkNeighborhood;


    @Column( "use_price")
    private Double usePrice;

    @Column( "fk_owner")
    private UUID fkOwner;


    private Point ubication;

    private DayOfWeek collectionRecurrentDay;

    private String description;
    

  
    
}