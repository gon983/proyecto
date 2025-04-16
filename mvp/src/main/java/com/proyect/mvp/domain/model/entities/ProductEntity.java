package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

import java.util.UUID;

@Builder
@Table("product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column("id_product")
    private UUID idProduct; // Use UUID

    @Column("name")
    private String name;

    

    @Column("photo")
    private String photo;

    @Column("unit_measurement")
    private String unitMeasurement;

    @Column("fk_category")
    private UUID fkCategory;

   
    
    
    private Double unity_price;
    @Column("unity_cost")
    private Double unityCost;
    @Column("cantidad_vendida_semana")
    private Double cantidadVendidaSemana;
    @Column("dinero_generado_semana")
    private Double dineroGeneradoSemana;
    
  

    

    public void setPrice(Double unitPrice){
        this.unity_price = unitPrice;
    }


   
}