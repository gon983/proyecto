package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "collectionpoint")
@Getter
@NoArgsConstructor
public class CollectionPointEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_collection_point")
    private String idCollectionPoint;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_neighborhood")
    private NeighborhoodEntity neighborhood;

    @Column(name = "use_price")
    private String usePrice;

    @ManyToOne
    @JoinColumn(name = "fk_owner")
    private UserEntity owner;
}