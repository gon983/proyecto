package com.proyect.mvp.application.services;



import com.proyect.mvp.application.dtos.create.DefaultDonationCreateDTO;
import com.proyect.mvp.application.dtos.update.DefaultDonationUpdateDTO;
import com.proyect.mvp.domain.model.entities.DefaultDonationEntity;
import com.proyect.mvp.domain.repository.DefaultDonationRepository;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DefaultDonationService {

    private final DefaultDonationRepository defaultDonationRepository;

    public DefaultDonationService(DefaultDonationRepository defaultDonationRepository) {
        this.defaultDonationRepository = defaultDonationRepository;
    }

    public Flux<DefaultDonationEntity> getDefaultDonationsByOrganization(UUID fkOrganization) {
        return defaultDonationRepository.findByFkOrganization(fkOrganization);
    }

    public Mono<DefaultDonationEntity> createDefaultDonation(DefaultDonationCreateDTO donationDTO) {
        DefaultDonationEntity donation = DefaultDonationEntity.builder()
                .idDefaultDonation(UUID.randomUUID())
                .fkOrganization(donationDTO.getFkOrganization())
                .level(donationDTO.getLevel())
                .build();
        
        return defaultDonationRepository.insertDefaultDonation(donation.getIdDefaultDonation(), donation.getFkOrganization(), donation.getLevel());
    }

    public Mono<Void> updateDefaultDonation(DefaultDonationUpdateDTO donationDTO) {
        return defaultDonationRepository.updateDefaultDonation(donationDTO.getIdDefaultDonation(), donationDTO.getFkOrganization(), donationDTO.getLevel());
    }

    public Mono<DefaultDonationEntity> updateDefaultDonationAlternative(DefaultDonationUpdateDTO donationDTO) {
        return defaultDonationRepository.findById(donationDTO.getIdDefaultDonation())
                .flatMap(donation -> {
                    DefaultDonationEntity newDonation = new DefaultDonationEntity().builder()
                                                                                .idDefaultDonation(donation.getIdDefaultDonation())
                                                                                .fkOrganization(donationDTO.getFkOrganization())
                                                                                .level(donationDTO.getLevel())
                                                                                .build();
                    return defaultDonationRepository.save(newDonation);
                });
    }
}
