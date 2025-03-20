package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "sale_detail")
@Getter
@NoArgsConstructor
public class BigSaleEntity {

    @Id
    
    @Column( "id_sale_detail")
    private String idBigSale;

    
    
    private ProductEntity  product;

    @Column( "quantity")
    private double quantity;

    
    
    private SaleEntity  sale;
}