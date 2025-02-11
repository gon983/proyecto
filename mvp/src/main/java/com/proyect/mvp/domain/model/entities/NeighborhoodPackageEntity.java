package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;


@Table( "neighborhoodpackage")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageEntity {

    @Id
    
    @Column( "id_neighborhood_package")
    private String idNeighborhoodPackage;

    
    (name = "fk_in_charge")
    private UserEntity  inCharge;

    
    (name = "fk_collection_point")
    private CollectionPointEntity  collectionPoint;

    @Column( "date")
    private LocalDateTime date;
}