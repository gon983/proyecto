package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "productprice")
@Getter
@NoArgsConstructor
public class ProductPriceEntity {

    @Id
    
    @Column( "id_product_price")
    private String idProductPrice;

    
    
    private ProductEntity  product;

    
    
    private ProductStateEntity  productState;

    @Column( "price")
    private double price;

    @Column( "description")
    private String description;
}