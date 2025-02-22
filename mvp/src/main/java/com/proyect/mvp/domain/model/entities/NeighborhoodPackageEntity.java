package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;


@Table( "neighborhood_package")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageEntity {

    @Id
    
    @Column( "id_neighborhood_package")
    private String idNeighborhoodPackage;

    
    
    private UserEntity  inCharge;

    
    
    private CollectionPointEntity  collectionPoint;

    @Column( "date")
    private LocalDateTime date;
}