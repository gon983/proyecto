package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;


@Table( "stockmovement")
@Getter
@NoArgsConstructor
public class StockMovementEntity {

    @Id
    
    @Column( "id_stock_movement")
    private String idStockMovement;

    
    
    private ProductEntity  product;

    @Column( "quantity")
    private double quantity;

    
    @Column( "type")
    private StockMovementTypeEnum  type; 

    @Column( "date")
    private LocalDate date; 

    @Column( "coment")
    private String comment; 
}


