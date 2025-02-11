package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "saledetail")
@Getter
@NoArgsConstructor
public class SaleDetailEntity {

    @Id
    
    @Column( "id_sale_detail")
    private String idSaleDetail;

    
    (name = "fk_product")
    private ProductEntity  product;

    @Column( "quantity")
    private double quantity;

    
    (name = "fk_sale")
    private SaleEntity  sale;
}