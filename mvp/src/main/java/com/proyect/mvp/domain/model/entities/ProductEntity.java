package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "product")
@Getter
@NoArgsConstructor
public class ProductEntity {

    @Id
    
    @Column( "id_product")
    private String idProduct;

    @Column( "name")
    private String name;

    @Column( "stock")
    private double stock;

    @Column( "alert_stock")
    private double alertStock;

    @Column( "photo")
    private String photo;

    
    
    private ProductCategoryEntity  productCategory;
}