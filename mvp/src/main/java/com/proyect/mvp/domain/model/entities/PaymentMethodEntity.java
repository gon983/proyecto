package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "paymentmethod")
@Getter
@NoArgsConstructor
public class PaymentMethodEntity {

    @Id
    
    @Column( "id_payment_method")
    private String idPaymentMethod;

    @Column( "name")
    private String name;
}