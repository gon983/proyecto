package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Table("product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column("id_product")
    private UUID idProduct; // Use UUID

    @Column("name")
    private String name;

    @Column("stock")
    private double stock;

    @Column("alert_stock")
    private double alertStock;

    @Column("photo")
    private String photo;

    @Column("unit_measurement")
    private String unitMeasurement;

    @Column("fk_productor") // Use @Column for FK
    private UUID fkProductor; // Use UUID
    @Transient
    private List<ProductHistoryEntity> history;
    @Transient
    private ProductPriceEntity price;

    public void addHistory(List<ProductHistoryEntity> history){
        this.history = history;
    }
}