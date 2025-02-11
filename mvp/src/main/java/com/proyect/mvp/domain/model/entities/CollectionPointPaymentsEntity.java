package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "collectionpointpayments")
@Getter
@NoArgsConstructor
public class CollectionPointPaymentsEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_collection_point_payments")
    private String idCollectionPointPayments;

    @ManyToOne
    @JoinColumn(name = "fk_collection_point")
    private CollectionPointEntity  collectionPoint;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "note")
    private String note;

    @Column(name = "amount")
    private double amount;
}