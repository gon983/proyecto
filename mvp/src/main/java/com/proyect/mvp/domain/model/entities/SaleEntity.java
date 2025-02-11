package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "sale")
@Getter
@NoArgsConstructor
public class SaleEntity {

    @Id
    
    @Column( "id_sale")
    private String idSale;

    @Column( "amount")
    private double amount;

    
    (name = "fk_productor")
    private UserEntity  productor; // Assuming "productor" refers to the User entity

    
    (name = "fk_deliver_guy")
    private UserEntity  deliverGuy;

    
    (name = "fk_payment_method")
    private PaymentMethodEntity  paymentMethod;

    @Column( "bill")
    private String bill;
}