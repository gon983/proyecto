package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;


@Table( "salestate")
@Getter
@NoArgsConstructor
public class SaleStateEntity {

    @Id
    
    @Column( "id_sale_state")
    private String idSaleState;

    @Column( "name")
    private String name;
}