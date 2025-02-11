package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "purchase")
@Getter
@NoArgsConstructor
public class PurchaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_purchase")
    private String idPurchase;

    @ManyToOne
    @JoinColumn(name = "fk_user")
    private UserEntity  user;

    @ManyToOne
    @JoinColumn(name = "fk_type_purchase")
    private PurchaseTypeEntity  typePurchase;

    @Column(name = "amount")
    private double amount;

    @ManyToOne
    @JoinColumn(name = "fk_neighborhood_package")
    private NeighborhoodPackageEntity  neighborhoodPackage;

    @ManyToOne
    @JoinColumn(name = "fk_payment_method")
    private PaymentMethodEntity  paymentMethod;
}