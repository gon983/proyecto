package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;

@Entity
@Table(name = "stockmovement")
@Getter
@NoArgsConstructor
public class StockMovementEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_stock_movement")
    private String idStockMovement;

    @ManyToOne
    @JoinColumn(name = "fk_product")
    private ProductEntity  product;

    @Column(name = "quantity")
    private double quantity;

    @Enumerated(EnumType.STRING) 
    @Column(name = "type")
    private StockMovementTypeEnum  type; 

    @Column(name = "date")
    private LocalDate date; 

    @Column(name = "coment")
    private String comment; 
}


