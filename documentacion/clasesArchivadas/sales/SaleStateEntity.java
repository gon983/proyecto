package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Table("sale_state")
@Getter
@NoArgsConstructor
public class SaleStateEntity {

    @Id
    @Column("id_sale_state")
    private UUID idSaleState;

    @Column("name")
    private String name;

    public SaleStateEntity(String name) {
        this.idSaleState = UUID.randomUUID();
        this.name = name;
    }
}
