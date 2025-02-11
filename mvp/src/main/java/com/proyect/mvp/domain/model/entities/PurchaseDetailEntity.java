package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "purchasedetail")
@Getter
@NoArgsConstructor
public class PurchaseDetailEntity {

    @Id
    
    @Column( "id_purchase_detail")
    private String idPurchaseDetail;

    
    
    private ProductEntity  product;

    @Column( "quantity")
    private double quantity;

    
    
    private PurchaseEntity  purchase;
}