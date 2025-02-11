package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "productorxproduct")
@Getter
@NoArgsConstructor
public class ProductorxProductEntity {

    @Id
    
    @Column( "id_productxproductor")
    private String idProductxproductor;

    
    (name = "id_product")
    private ProductEntity  product;

    
    (name = "id_productor")
    private UserEntity  productor; // Assuming "productor" refers to the User entity
}