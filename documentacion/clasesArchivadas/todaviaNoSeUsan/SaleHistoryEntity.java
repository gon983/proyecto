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
@Table("sale_history")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SaleHistoryEntity {

    @Id
    @Column("id_sale_history")
    private UUID idSaleHistory;

    @Column("fk_sale") // Foreign key to sale
    private UUID idSale;

    @Column("fk_sale_state") // Foreign key to sale_state
    private UUID idSaleState;


    @Column("initial_date") // Correct column name
    private LocalDateTime initialDate;

    @Column("final_date") // Correct column name
    private LocalDateTime finalDate;

    
}