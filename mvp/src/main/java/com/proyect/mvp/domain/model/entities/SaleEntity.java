package com.proyect.mvp.domain.model.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import io.micrometer.common.lang.Nullable;

import org.springframework.data.relational.core.mapping.Column;


@Builder
@Table( "sale")
@Getter
@AllArgsConstructor
public class SaleEntity {

    @Id
    
    @Column( "id_sale")
    private String idSale;

    @Column( "amount")
    private double amount;
 
    private UUID  fkProductor; // Assuming "productor" refers to the User entity

    @Column("fk_product")
    UUID fkProduct;
    @Column("quantity")
    double quantity;
    @Column("unit_price")
    double unitPrice;
    @Column("fk_current_state")
    UUID fkCurrentState;
    @Column("fk_buyer")
    UUID fkBuyer;
    @Column("fk_collection_point")
    UUID fkCollectionPoint;
}