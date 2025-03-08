package com.proyect.mvp.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;

import reactor.core.publisher.Mono;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStateService purchaseStateService;
    private final PurchaseDetailService purchaseDetailService;
    private final UserService userService;
    private final String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseStateService purchaseStateService, PurchaseDetailService purchaseDetailService, UserService userService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
        this.purchaseDetailService = purchaseDetailService;
        this.userService = userService;
    }

    public Mono<PurchaseEntity> createPurchase(PurchaseCreateDTO purchaseDto) {
        return purchaseStateService.findByName("pending") // Buscar estado en la DB
            .switchIfEmpty(Mono.error(new IllegalStateException("Pending state not found"))) // Manejo de error si no existe
            .flatMap(purchaseState -> { 
                PurchaseEntity purchase = PurchaseEntity.builder()
                    .fkUser(purchaseDto.getFkUser())
                    .level(purchaseDto.getLevel())
                    .createdAt(LocalDateTime.now())
                    .fkCurrentState(purchaseState.getIdPurchaseState()) // Asignar estado encontrado
                    .build();
                return purchaseRepository.save(purchase); // Guardar compra
            });
    }

    public Mono<PurchaseEntity> confirmPurchase(UUID purchaseId, UUID userId) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> 
                purchaseDetailService.getDetailsFromPurchase(purchaseId)
                    .collectList()
                    .flatMap(details -> processPayments(purchase, details, userId))
                    .thenReturn(purchase)
            );
    }

private Mono<Void> processPayments(PurchaseEntity purchase, List<PurchaseDetailEntity> details, UUID userId) {
    return userService.getUserById(userId)
        .flatMap(user -> {
            try {
                MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
                PaymentClient paymentClient = new PaymentClient();

                for (PurchaseDetailEntity detail : details) {
                    PaymentCreateRequest paymentRequest = PaymentCreateRequest.builder()
                        .transactionAmount(BigDecimal.valueOf(detail.getUnitPrice() * detail.getQuantity()))
                        .description("Pago de " + detail.getProduct().getName())
                        .payer(PaymentPayerRequest.builder()
                            .email(user.getEmail())
                            .identification(IdentificationRequest.builder()
                                .type(user.getDocumentType())
                                .number(user.getDocumentNumber())
                                .build())
                            .build())
                        .paymentMethodId("visa")
                        .externalReference(purchase.getIdPurchase().toString())
                        .build();

                    Payment paymentResponse = paymentClient.create(paymentRequest);
                    System.out.println("Pago creado con ID: " + paymentResponse.getId());
                }
                return Mono.empty();
            } catch (MPException | MPApiException e) {
                return Mono.error(new RuntimeException("Error procesando pagos", e));
            }
        });
}


    public Mono<PurchaseEntity> getPurchaseWithDetails(UUID idPurchase){
        return purchaseRepository.findById(idPurchase)
                                .flatMap(
                                   purchase -> {
                                        return purchaseDetailService.getDetailsFromPurchase(idPurchase)
                                           .collectList()
                                           .map(details -> {
                                               purchase.addDetails(details);
                                               return purchase;
                                           });
                                   });
    }
}
