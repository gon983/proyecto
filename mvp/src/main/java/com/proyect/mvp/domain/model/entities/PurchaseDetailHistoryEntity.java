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
@Table("purchase_detail_history")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PurchaseDetailHistoryEntity {

    @Id
    @Column("id_purchase_detail_history")
    private UUID idPurchaseDetailHistory;

    @Column("id_purchase_detail") // Foreign key to purchase_detail
    private UUID idPurchaseDetail;

    @Column("id_purchase_detail_state") // Foreign key to purchase_detail_state
    private UUID idPurchaseDetailState;

    @Column("previous_purchase_detail_state") // Previous state for history tracking
    private UUID previousPurchaseDetailState;

    @Column("change_date")
    private LocalDateTime changeDate;

    
}