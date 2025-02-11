package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "neighborhoodpackagestate")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageStateEntity {

    @Id
    
    @Column( "id_neighborhood_package_state")
    private String idNeighborhoodPackageState;

    @Column( "name")
    private String name;
}