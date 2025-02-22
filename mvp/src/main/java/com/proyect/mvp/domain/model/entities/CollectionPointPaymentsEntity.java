package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;


@Table( "collection_point_payments")
@Getter
@NoArgsConstructor
public class CollectionPointPaymentsEntity {

    @Id
    
    @Column( "id_collection_point_payments")
    private String idCollectionPointPayments;

    
    
    private CollectionPointEntity  collectionPoint;

    @Column( "date")
    private LocalDateTime date;

    @Column( "note")
    private String note;

    @Column( "amount")
    private double amount;
}