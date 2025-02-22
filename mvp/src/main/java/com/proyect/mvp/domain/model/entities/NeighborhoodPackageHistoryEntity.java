package com.proyect.mvp.domain.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Table("neighborhood_package_history") // Use snake_case for table names
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class NeighborhoodPackageHistoryEntity {

    @Id
    @Column("id_neighborhood_package_history")
    private UUID idNeighborhoodPackageHistory;

    @Column("fk_neighborhood_package") // Foreign key to neighborhood_package
    private UUID idNeighborhoodPackage;

    @Column("fk_package_state") // Foreign key to package_state
    private UUID idPackageState;

    @Column("description")
    private String description;

    @Column("initial_date")
    private LocalDateTime init;

    @Column("final_date")
    private LocalDateTime finish;
}