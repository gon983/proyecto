package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "purchasetype")
@Getter
@NoArgsConstructor
public class PurchaseTypeEntity {

    @Id
    
    @Column( "id_purchase_type")
    private String idPurchaseType;

    @Column( "name")
    private String name;
}