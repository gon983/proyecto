package com.proyect.mvp.domain.model.entities;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Builder
@Table( "collectionpoint")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionPointEntity {

    @Id
    
    @Column( "id_collection_point")
    private UUID idCollectionPoint;

    @Column( "name")
    private String name;

    private UUID fk_neighborhood;


    @Column( "use_price")
    private Double usePrice;

    private UUID fk_owner;


    private Point ubication;

    private String description;
    @Transient
    private List<CollectionPointHistoryEntity> history; 

    public void addHistory(List<CollectionPointHistoryEntity> history){
        this.history = history;

    }
    
}