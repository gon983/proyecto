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
@Table("purchasestate")
@Getter
@NoArgsConstructor
public class PurchaseStateEntity {

    @Id
    @Column("id_purchase_state")
    private UUID idPurchaseState;

    @Column("name")
    private String name;

    public PurchaseStateEntity(String name) {
        this.idPurchaseState = UUID.randomUUID();
        this.name = name;
    }
}