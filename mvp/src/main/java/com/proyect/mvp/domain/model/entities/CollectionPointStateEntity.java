package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "collectionpointstate")
@Getter
@NoArgsConstructor
public class CollectionPointStateEntity {

    @Id
    
    @Column( "id_collection_point_state")
    private String idCollectionPointState;

    @Column( "name")
    private String name;

}