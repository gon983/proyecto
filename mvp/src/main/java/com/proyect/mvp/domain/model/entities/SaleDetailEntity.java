package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "saledetail")
@Getter
@NoArgsConstructor
public class SaleDetailEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_sale_detail")
    private String idSaleDetail;

    @ManyToOne
    @JoinColumn(name = "fk_product")
    private Product product;

    @Column(name = "quantity")
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "fk_sale")
    private Sale sale;
}