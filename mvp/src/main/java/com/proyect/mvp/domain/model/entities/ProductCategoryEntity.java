package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "productcategory")
@Getter
@NoArgsConstructor
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_product_category")
    private String idProductCategory;

    @Column(name = "name")
    private String name;

    @Column(name = "measurement_unity")
    private String measurementUnity;
}