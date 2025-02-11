package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "productorxproduct")
@Getter
@NoArgsConstructor
public class ProductorxProductEntity {

    @Id
    
    @Column( "id_productxproductor")
    private String idProductxproductor;

    
    
    private ProductEntity  product;

    
    
    private UserEntity  productor; // Assuming "productor" refers to the User entity
}