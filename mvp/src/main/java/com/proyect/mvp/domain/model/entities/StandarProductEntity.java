package com.proyect.mvp.domain.model.entities;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("standar_product")
@Getter
@AllArgsConstructor
//@NoArgsConstructor
@Builder 
public class StandarProductEntity {
    @Id
    @Column("id_standar_product")
    private final UUID idStandarProduct;
    @Column("name")
    private String name;
    @Column("fk_category")
    private UUID fkCategory; 
    
}
