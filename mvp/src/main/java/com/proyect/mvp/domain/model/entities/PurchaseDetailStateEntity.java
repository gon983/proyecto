package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Table("purchasedetailstate")
@Getter
@NoArgsConstructor
public class PurchaseDetailStateEntity {

    @Id
    @Column("id_purchase_detail_state")
    private UUID idPurchaseDetailState;

    @Column("name")
    private String name;

    public PurchaseDetailStateEntity(String name) {
        this.idPurchaseDetailState = UUID.randomUUID();
        this.name = name;
    }
}