package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "neighborhoodpackage")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_neighborhood_package")
    private String idNeighborhoodPackage;

    @ManyToOne
    @JoinColumn(name = "fk_in_charge")
    private UserEntity  inCharge;

    @ManyToOne
    @JoinColumn(name = "fk_collection_point")
    private CollectionPointEntity  collectionPoint;

    @Column(name = "date")
    private LocalDateTime date;
}