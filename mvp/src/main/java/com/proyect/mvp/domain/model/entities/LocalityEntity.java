package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "locality")
@Getter
@NoArgsConstructor
public class LocalityEntity {

    @Id
    
    @Column( "id_locality")
    private String idLocality;

    @Column( "name")
    private String name;

    
    
    private CityEntity  city; // Assuming you have a City entity
}