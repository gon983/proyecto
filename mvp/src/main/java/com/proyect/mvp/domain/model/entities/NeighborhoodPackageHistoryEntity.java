package com.proyect.mvp.domain.model.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;


@Table( "neighborhoodpackagehistory")
@Getter
@NoArgsConstructor
public class NeighborhoodPackageHistoryEntity {

    @Id
    
    @Column( "id_neighborhood_package_history")
    private String idNeighborhoodPackageHistory;

    
    (name = "fk_neighborhood_package")
    private NeighborhoodPackageEntity  neighborhoodPackage;

    
    (name = "fk_neighborhood_package_state")
    private NeighborhoodPackageStateEntity  neighborhoodPackageState;

    @Column( "description")
    private String description;

    @Column( "init")
    private LocalDateTime init;

    @Column( "finish")
    private LocalDateTime finish;
}