package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "productstate")
@Getter
@NoArgsConstructor
public class ProductStateEntity {

    @Id
    
    @Column( "id_product_state")
    private String idProductState;

    @Column( "name")
    private String name;
}