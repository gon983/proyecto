package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_product")
    private String idProduct;

    @Column(name = "name")
    private String name;

    @Column(name = "stock")
    private double stock;

    @Column(name = "alert_stock")
    private double alertStock;

    @Column(name = "photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "fk_product_category")
    private ProductCategory productCategory;
}