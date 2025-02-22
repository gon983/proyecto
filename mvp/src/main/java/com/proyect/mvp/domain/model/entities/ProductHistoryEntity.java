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
@Table("product_history")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ProductHistoryEntity {

    @Id
    @Column("id_product_history")
    private UUID idProductHistory;

    @Column("fk_product") // Foreign key to product
    private UUID idProduct;

    @Column("fk_product_state") // Foreign key to product_state
    private UUID idProductState;

    

    @Column("change_date")
    private LocalDateTime changeDate;

    // You might include other relevant information in the history,
    // e.g., the user who made the change.  Add those fields here.
    @Column("changed_by")
    private UUID changedBy; // Example: User ID who made the change
}