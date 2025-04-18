package com.proyect.mvp.domain.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.proyect.mvp.domain.model.entities.DeviceTokenEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DeviceTokenRepository extends R2dbcRepository<DeviceTokenEntity, UUID> {
    Mono<DeviceTokenEntity> findByUserIdAndDeviceType(UUID userId, String deviceType);
    Flux<DeviceTokenEntity> findByUserId(UUID userId);
}