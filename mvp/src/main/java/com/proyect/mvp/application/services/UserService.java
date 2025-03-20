package com.proyect.mvp.application.services;

import com.proyect.mvp.application.dtos.create.UserCreateDTO;
import com.proyect.mvp.application.dtos.update.UserUpdateDTO;
import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.domain.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final NeighborhoodService neighborhoodService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, NeighborhoodService neighborhoodService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.neighborhoodService = neighborhoodService;
    }

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> getUserById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<UserEntity> saveNewUser(UserCreateDTO userDto) {
        UserEntity userEntity = UserEntity.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .createdAt(LocalDateTime.now())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .documentType(userDto.getDocumentType())
                .documentNumber(userDto.getDocumentNumber())
                .fkNeighborhood(userDto.getFkNeighborhood())
                .phone(userDto.getPhone())
                .roleOne(userDto.getRoleOne())
                .fkCollectionPointSuscribed(userDto.getFkCollectionPointSuscribed())
                .build();
        return userRepository.save(userEntity)
                .thenReturn(userEntity)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error saving user", error));
    }

    public Mono<UserEntity> updateUser(UUID id, UserUpdateDTO userDto) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    UserEntity userEntity = UserEntity.builder()
                            .idUser(id)  // Mantener el mismo ID
                            .username(userDto.getUsername())
                            .email(userDto.getEmail())
                            .createdAt(existingUser.getCreatedAt()) // Mantener la fecha original
                            .firstName(userDto.getFirstName())
                            .lastName(userDto.getLastName())
                            .documentType(userDto.getDocumentType())
                            .documentNumber(userDto.getDocumentNumber())
                            .fkNeighborhood(userDto.getFkNeighborhood())
                            .phone(userDto.getPhone())
                            .roleOne(userDto.getRoleOne())
                            .fkCollectionPointSuscribed(userDto.getFkCollectionPointSuscribed())
                            .mpAccessTokenEncrypted(encryptionService.encrypt(userDto.getMpAccessToken()))
                            .mpPublicKeyEncrypted(encryptionService.encrypt(userDto.getMpPublicKey()))
                            .build();
                    

                    return userRepository.save(userEntity); // Retornar el nuevo usuario guardado
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<UUID> getLocality(UUID idUser){
        return getUserById(idUser)
                .flatMap(user -> neighborhoodService.getNeighborhoodById(user.getFkNeighborhood()))
                .flatMap(neighborhood -> Mono.just(neighborhood.getLocalityId()));
    }

    public Mono<String> getMpAccessToken(UUID userId) {
        return userRepository.findById(userId)
                             .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado: " + userId)))
                             .map(user -> encryptionService.decrypt(user.getMpAccessTokenEncrypted()));
    }

    public Mono<? extends Void> saveProducerMpData(UUID productorId, String accessTokenProductor, String userProductorMpId,
            String refreshProductorToken) {
        return userRepository.saveProducerMpData(productorId, accessTokenProductor, userProductorMpId,refreshProductorToken);
    }

    public Mono<String> getMpProductorUserId(UUID productorId) {
        return userRepository.findById(productorId)
                            .flatMap(productor-> Mono.just(productor.getUserProductorMpId()));
    }

    public Mono<String> getMpRefreshToken(UUID productorId) {
        return userRepository.findById(productorId)
                            .flatMap(productor-> Mono.just(productor.getRefreshProductorToken()));
    }

    public Mono<Void> updateProducerMpTokens(UUID productorId,String newAccessToken,String newRefreshToken){
        return userRepository.updateProducerMpTokens(productorId, newAccessToken, newRefreshToken);
    }



    
}

