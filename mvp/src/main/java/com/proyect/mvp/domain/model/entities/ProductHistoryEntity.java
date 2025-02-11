package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;


@Table( "producthistory")
@Getter
@NoArgsConstructor
public class ProductHistoryEntity {

    @Id
    
    @Column( "id_product_history")
    private String idProductHistory;

    
    
    private ProductEntity  product;

    
    
    private ProductStateEntity  productState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}