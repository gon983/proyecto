package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "purchase")
@Getter
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    
    @Column( "id_purchase")
    private String idPurchase;

    
    (name = "fk_user")
    private UserEntity  user;

    
    (name = "fk_type_purchase")
    private PurchaseTypeEntity  typePurchase;

    @Column( "amount")
    private double amount;

    
    (name = "fk_neighborhood_package")
    private NeighborhoodPackageEntity  neighborhoodPackage;

    
    (name = "fk_payment_method")
    private PaymentMethodEntity  paymentMethod;
}