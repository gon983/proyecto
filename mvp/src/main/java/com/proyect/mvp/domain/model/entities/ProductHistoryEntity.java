package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;


@Table( "producthistory")
@Getter
@NoArgsConstructor
public class ProductHistoryEntity {

    @Id
    
    @Column( "id_product_history")
    private String idProductHistory;

    
    (name = "fk_product")
    private ProductEntity  product;

    
    (name = "fk_product_state")
    private ProductStateEntity  productState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}