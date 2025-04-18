package com.proyect.mvp.application.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.proyect.mvp.application.dtos.create.DeviceTokenCreateDTO;
import com.proyect.mvp.domain.model.entities.DeviceTokenEntity;
import com.proyect.mvp.domain.repository.DeviceTokenRepository;
import com.proyect.mvp.domain.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class DeviceTokenService {
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository, UserRepository userRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.userRepository = userRepository;
    }

    public Mono<DeviceTokenEntity> saveDeviceToken(DeviceTokenCreateDTO tokenDTO) {
        return userRepository.findById(tokenDTO.getUserId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
                .flatMap(user -> {
                    // Primero buscamos si ya existe un token para este usuario/dispositivo
                    return deviceTokenRepository.findByUserIdAndDeviceType(tokenDTO.getUserId(), tokenDTO.getDeviceType())
                            .flatMap(existingToken -> {
                                // Si existe, actualizamos el token
                                DeviceTokenEntity updatedToken = DeviceTokenEntity.builder()
                                        .id(existingToken.getId())
                                        .userId(tokenDTO.getUserId())
                                        .deviceToken(tokenDTO.getDeviceToken())
                                        .deviceType(tokenDTO.getDeviceType())
                                        .createdAt(existingToken.getCreatedAt())
                                        .build();
                                
                                return deviceTokenRepository.save(updatedToken);
                            })
                            .switchIfEmpty(
                                // Si no existe, creamos uno nuevo
                                Mono.defer(() -> {
                                    DeviceTokenEntity newToken = DeviceTokenEntity.builder()
                                            .id(UUID.randomUUID())
                                            .userId(tokenDTO.getUserId())
                                            .deviceToken(tokenDTO.getDeviceToken())
                                            .deviceType(tokenDTO.getDeviceType())
                                            .createdAt(LocalDateTime.now())
                                            .build();
                                    
                                    return deviceTokenRepository.save(newToken);
                                })
                            );
                });
    }

    public Mono<Void> deleteDeviceToken(UUID userId, String deviceType) {
        return deviceTokenRepository.findByUserIdAndDeviceType(userId, deviceType)
                .flatMap(token -> deviceTokenRepository.deleteById(token.getId()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado")));
    }
}