package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "neighborhood")
@Getter
@NoArgsConstructor
public class NeighborhoodEntity {

    @Id
    
    @Column( "id_neighborhood")
    private String idNeighborhood;

    @Column( "name")
    private String name;

    
    
    private LocalityEntity  locality; 
}