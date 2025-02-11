package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "neighborhoodpackagestate")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageStateEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_neighborhood_package_state")
    private String idNeighborhoodPackageState;

    @Column(name = "name")
    private String name;
}