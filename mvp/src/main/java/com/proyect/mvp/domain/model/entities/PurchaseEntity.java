package com.proyect.mvp.domain.model.entities;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("purchase")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    @Column("id_purchase")
    private UUID idPurchase;

    @Column("fk_user")
    private UUID fkUser;

    @Column("amount")
    private double amount;

    @Column("fk_neighborhood_package")
    private UUID fkNeighborhoodPackage;

    @Column("fk_payment_method")
    private UUID fkPaymentMethod;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("level")
    private String level;

    @Transient
    private Set<PurchaseDetailEntity> details;
}