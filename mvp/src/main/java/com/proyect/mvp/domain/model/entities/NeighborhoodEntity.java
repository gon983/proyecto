package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "neighborhood")
@Getter
@NoArgsConstructor
public class NeighborhoodEntity {

    @Id
    
    @Column( "id_neighborhood")
    private String idNeighborhood;

    @Column( "name")
    private String name;

    
    (name = "fk_locality")
    private LocalityEntity  locality; 
}