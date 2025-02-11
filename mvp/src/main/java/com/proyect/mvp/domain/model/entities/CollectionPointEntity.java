package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "collectionpoint")
@Getter
@NoArgsConstructor
public class CollectionPointEntity {

    @Id
    
    @Column( "id_collection_point")
    private String idCollectionPoint;

    @Column( "name")
    private String name;

    
    (name = "fk_neighborhood")
    private NeighborhoodEntity neighborhood;

    @Column( "use_price")
    private String usePrice;

    
    (name = "fk_owner")
    private UserEntity owner;
}