package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;
import java.util.UUID;


@Table( "stock_movement")
@Getter
@NoArgsConstructor
public class StockMovementEntity {

    @Id
    
    @Column( "id_stock_movement")
    private String idStockMovement;

    @Column("fk_product")
    private UUID fkProduct;
    @Transient
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


