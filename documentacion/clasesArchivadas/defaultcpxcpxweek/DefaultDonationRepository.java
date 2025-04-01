package com.proyect.mvp.domain.repository;


import com.proyect.mvp.domain.model.entities.DefaultDonationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface DefaultDonationRepository extends R2dbcRepository<DefaultDonationEntity, UUID> {

    Flux<DefaultDonationEntity> findByFkOrganization(UUID fkOrganization);

    @Query("INSERT INTO default_donation (id_default_donation, fk_organization, level) " +
            "VALUES (:idDefaultDonation, :fkOrganization, :level)")
    Mono<DefaultDonationEntity> insertDefaultDonation(UUID idDefaultDonation, UUID fkOrganization, int level);

    @Query("UPDATE default_donation SET fk_organization = :fkOrganization, level = :level WHERE id_default_donation = :idDefaultDonation")
    Mono<Void> updateDefaultDonation(UUID idDefaultDonation, UUID fkOrganization, int level);

    Mono<DefaultDonationEntity> findById(UUID id);
}