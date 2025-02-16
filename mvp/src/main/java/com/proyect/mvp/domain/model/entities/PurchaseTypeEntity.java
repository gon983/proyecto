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
@Table("purchasetype")
@Getter
@NoArgsConstructor
public class PurchaseTypeEntity {

    @Id
    @Column("id_purchase_type")
    private UUID idPurchaseType;

    @Column("name")
    private String name;

    public PurchaseTypeEntity(String name) {
        this.idPurchaseType = UUID.randomUUID();
        this.name = name;
    }
}
