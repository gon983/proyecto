package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table("purchase_history")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PurchaseHistoryEntity {

    @Id
    @Column("id_purchase_history")
    private UUID idPurchaseHistory;

    @Column("fk_purchase") // Foreign key to purchase
    private UUID idPurchase;

    @Column("fk_purchase_state") // Foreign key to purchase_state
    private UUID idPurchaseState;

    @Column("previous_purchase_state") // Previous state for history tracking
    private UUID previousPurchaseState;

    @Column("initial_date") // Correct column name
    private LocalDateTime initialDate;

    @Column("final_date") // Correct column name
    private LocalDateTime finalDate;

    @Column("changed_by") // User who made the change
    private UUID changedBy;
}