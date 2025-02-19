package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "purchasedetail")
@Getter
@NoArgsConstructor
public class PurchaseDetailEntity {

    @Id
    @Column( "id_purchase_detail")
    private String idPurchaseDetail;
    @Transient
    private ProductEntity  product;
    @Column("fk_product")
    private UUID fkProduct;
    @Column("fk_puchase")
    private UUID fkPurchase;

    @Column( "quantity")
    private double quantity;
    @Column("unit_price")
    private double unitPrice;
    @Column("created_by")
    private UUID createdBy;
    @Column("updatedBY")
    private UUID updated_by;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updatedAt")
    private LocalDateTime updatedAt;

    
}