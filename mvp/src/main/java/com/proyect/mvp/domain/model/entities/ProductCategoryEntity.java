package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "productcategory")
@Getter
@NoArgsConstructor
public class ProductCategoryEntity {

    @Id
    
    @Column( "id_product_category")
    private String idProductCategory;

    @Column( "name")
    private String name;

    @Column( "measurement_unity")
    private String measurementUnity;
}