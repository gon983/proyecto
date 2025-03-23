package com.proyect.mvp.domain.model.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;


@Builder
@AllArgsConstructor
@Table( "purchase_detail")
@Getter
@NoArgsConstructor
public class PurchaseDetailEntity {

    @Id
    @Column( "id_purchase_detail")
    private UUID idPurchaseDetail;
    @Transient
    private ProductEntity  product;
    @Column("fk_product")
    private UUID fkProduct;
    @Column("fk_purchase")
    private UUID fkPurchase;
    @Column("fk_state")
    private UUID fkState;

    @Column( "quantity")
    private double quantity;
    @Column("unit_price")
    private double unitPrice;
    @Column("created_by")
    private UUID createdBy;
    @Column("updated_by")
    private UUID updatedBy;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;

    
    public void addProduct(ProductEntity productEntity){
        this.product = productEntity;
    }

    public double calculatePrice(){
        return this.unitPrice * this.quantity;
    }

    public String getIdPurchaseDetail(){
        return idPurchaseDetail.toString();
    }

    public void setFkPurchase(UUID idPurchase){
        this.fkPurchase = idPurchase;
    }

    @Override
    public String toString(){
        return "| Product: " + fkProduct +
                "unitPrice" + unitPrice +
                "quantity" + quantity + "|";
    }

    public void setFkCurrentState(UUID currentState){
        this.fkState = currentState;
    }
}