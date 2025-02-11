package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "purchasedetail")
@Getter
@NoArgsConstructor
public class PurchaseDetailEntity {

    @Id
    
    @Column( "id_purchase_detail")
    private String idPurchaseDetail;

    
    (name = "fk_product")
    private ProductEntity  product;

    @Column( "quantity")
    private double quantity;

    
    (name = "fk_purchase")
    private PurchaseEntity  purchase;
}