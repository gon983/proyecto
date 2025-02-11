package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "productorxproduct")
@Getter
@NoArgsConstructor
public class ProductorxProductEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_productxproductor")
    private String idProductxproductor;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductEntity  product;

    @ManyToOne
    @JoinColumn(name = "id_productor")
    private UserEntity  productor; // Assuming "productor" refers to the User entity
}