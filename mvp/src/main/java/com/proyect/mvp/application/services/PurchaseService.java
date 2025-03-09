package com.proyect.mvp.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.core.MPRequestOptions;
import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStateService purchaseStateService;
    private final PurchaseDetailService purchaseDetailService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseStateService purchaseStateService, PurchaseDetailService purchaseDetailService, UserService userService, EncryptionService encryptionService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
        this.purchaseDetailService = purchaseDetailService;
        this.userService = userService;
        this.encryptionService = encryptionService;
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

    public Mono<PurchaseEntity> confirmPurchase(UUID purchaseId) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> 
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchaseId)
                    .collectList()
                    .flatMap(details -> processPayments(purchase, details, purchase.getFkUser()))
                    .thenReturn(purchase)
            );
    }

    private Mono<Preference> processPayments(PurchaseEntity purchase, List<PurchaseDetailEntity> details, UUID userId) {
    return userService.getUserById(userId)
        .flatMap(buyer -> {
            String buyerAccessToken = encryptionService.decrypt(buyer.getMpAccessToken());

            if (buyerAccessToken == null || buyerAccessToken.isEmpty()) {
                return Mono.error(new RuntimeException("El comprador no tiene configurado un token de acceso de MercadoPago"));
            }

            if (details.isEmpty()) {
                return Mono.error(new RuntimeException("No hay detalles de compra para procesar"));
            }

            UUID producerId = details.get(0).getProduct().getFkProductor();

            return userService.getUserById(producerId)
                .flatMap(seller -> {
                    String sellerAccessToken = encryptionService.decrypt(seller.getMpAccessToken());

                    if (sellerAccessToken == null || sellerAccessToken.isEmpty()) {
                        return Mono.error(new RuntimeException("El vendedor no tiene configurado un token de acceso de MercadoPago"));
                    }

                    return Mono.fromCallable(() -> {
                        try {
                            // Configurar MercadoPago
                            MercadoPagoConfig.setAccessToken(buyerAccessToken);
                            PreferenceClient preferenceClient = new PreferenceClient();

                            // Crear la lista de items
                            List<PreferenceItemRequest> items = new ArrayList<>();
                            for (PurchaseDetailEntity detail : details) {
                                PreferenceItemRequest item = PreferenceItemRequest.builder()
                                    .title(detail.getProduct().getName())
                                    .unitPrice(BigDecimal.valueOf(detail.getUnitPrice()))
                                    .quantity((int) detail.getQuantity())
                                    .build();
                                items.add(item);
                            }

                            // Crear la Preference
                            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                .items(items)
                                .payer(PreferencePayerRequest.builder()
                                    .email(buyer.getEmail())
                                    .build())
                                .notificationUrl("https://www.your-site.com/ipn") // Reemplaza con tu URL de notificación
                                .backUrls(PreferenceBackUrlsRequest.builder()
                                    .success("https://www.success.com") // Reemplaza con tu URL de éxito
                                    .failure("http://www.failure.com") // Reemplaza con tu URL de fallo
                                    .pending("http://www.pending.com") // Reemplaza con tu URL de pendiente
                                    .build())
                                .build();

                            MPRequestOptions requestOptions = MPRequestOptions.builder()
                                .accessToken(buyerAccessToken)
                                //.useSandbox(true)
                                .build();

                            Preference preference = preferenceClient.create(preferenceRequest, requestOptions);

                            System.out.println("Preference creada con ID: " + preference.getId());
                            return preference;
                        } catch (MPApiException mpApiError) {
                            String errorDetails = "Código: " + mpApiError.getStatusCode();
                            if (mpApiError.getApiResponse() != null) {
                                errorDetails += ", Mensaje: " + mpApiError.getApiResponse().getContent();
                            }
                            System.err.println("Error API MercadoPago: " + errorDetails);
                            throw new RuntimeException("Error en API MercadoPago: " + errorDetails, mpApiError);
                        } catch (MPException mpError) {
                            System.err.println("Error SDK MercadoPago: " + mpError.getMessage());
                            throw new RuntimeException("Error en SDK MercadoPago", mpError);
                        }
                    })
                    .subscribeOn(Schedulers.boundedElastic());
                });
        })
        .onErrorResume(e -> {
            System.err.println("Error procesando pagos: " + e.getMessage());
            e.printStackTrace();
            return Mono.error(new RuntimeException("Error procesando pagos", e));
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
