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
@Table("product_state")
@Getter
@NoArgsConstructor
public class ProductStateEntity {

    @Id
    @Column("id_product_state")
    private UUID idProductState;

    @Column("name")
    private String name;

    public ProductStateEntity(String name) {
        this.idProductState = UUID.randomUUID();
        this.name = name;
    }
}
