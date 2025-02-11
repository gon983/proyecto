package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "purchase")
@Getter
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    
    @Column( "id_purchase")
    private String idPurchase;

    
    
    private UserEntity  user;

    
    
    private PurchaseTypeEntity  typePurchase;

    @Column( "amount")
    private double amount;

    
    
    private NeighborhoodPackageEntity  neighborhoodPackage;

    
    
    private PaymentMethodEntity  paymentMethod;
}