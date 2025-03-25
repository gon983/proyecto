package com.proyect.mvp.domain.model.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Table( "stock_movement")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StockMovementEntity {

    @Id
    
    @Column( "id_stock_movement")
    private UUID idStockMovement;

    @Column("fk_product")
    private UUID fkProduct;

    @Column("fk_user")
    private UUID fkUser; //el buyer

    @Column( "quantity")
    private double quantity;

    
    @Column("type")
    private StockMovementTypeEnum type;

    @Column( "date")
    private LocalDateTime date; 

    @Column( "comment")
    private String comment; 
}


