package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;



@Table( "sale")
@Getter
@NoArgsConstructor
public class SaleEntity {

    @Id
    
    @Column( "id_sale")
    private String idSale;

    @Column( "amount")
    private double amount;

    
    
    private UserEntity  productor; // Assuming "productor" refers to the User entity

    
    
    private UserEntity  deliverGuy;

    
    
    private PaymentMethodEntity  paymentMethod;

    @Column( "bill")
    private String bill;
}