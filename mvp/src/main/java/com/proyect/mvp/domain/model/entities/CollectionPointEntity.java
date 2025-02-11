package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "collectionpoint")
@Getter
@NoArgsConstructor
public class CollectionPointEntity {

    @Id
    
    @Column( "id_collection_point")
    private String idCollectionPoint;

    @Column( "name")
    private String name;

    
    
    private NeighborhoodEntity neighborhood;

    @Column( "use_price")
    private String usePrice;

    
    
    private UserEntity owner;
}