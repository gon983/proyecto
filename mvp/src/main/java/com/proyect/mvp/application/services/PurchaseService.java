package com.proyect.mvp.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.common.AddressRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPApiException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.mercadopago.resources.preference.Preference;
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

    public Flux<Preference> confirmPurchase(UUID purchaseId) {
        return purchaseRepository.findById(purchaseId)
            .flatMapMany(purchase -> 
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchaseId)
                    .flatMap(detail -> processPayment(detail, purchase.getFkUser()))
            );
    }
    

    private Mono<Preference> processPayment(PurchaseDetailEntity detail, UUID userId) {
        return userService.getUserById(userId)
                    .flatMap(user -> {


                        MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
        

    
                        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                            .id(detail.getIdPurchaseDetail())
                            .title(detail.getProduct().getName())
                            .currencyId("ARS")
                            .pictureUrl("https://www.mercadopago.com/org-img/MP3/home/logomp3.gif")
                            .description("")
                            .categoryId("food")
                            .quantity((int) detail.getQuantity())
                            .unitPrice(new BigDecimal(detail.getUnitPrice()))
                            .build();
                    
                        List<PreferenceItemRequest> items = List.of(itemRequest);
                    
                        PreferencePayerRequest payer = PreferencePayerRequest.builder()
                            .name(user.getFirstName())
                            .surname(user.getLastName())
                            .email(user.getEmail())
                            .phone(PhoneRequest.builder().areaCode("54").number(user.getPhone()).build())
                            .identification(IdentificationRequest.builder().type(user.getDocumentType()).number(user.getDocumentNumber()).build())
                            .address(AddressRequest.builder().streetName("Street").zipCode("06233200").build())
                            .build();
                    
                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                            .success("https://www.success.com")
                            .failure("http://www.failure.com")
                            .pending("http://www.pending.com")
                            .build();
                    
                        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                            .excludedPaymentMethods(List.of())
                            .excludedPaymentTypes(List.of())
                            .build();
                    
                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                            .items(items)
                            .payer(payer)
                            .backUrls(backUrls)
                            .autoReturn("approved")
                            .paymentMethods(paymentMethods)
                            .notificationUrl("https://www.your-site.com/ipn")
                            .statementDescriptor("MEUNEGOCIO")
                            .externalReference("Reference_1234")
                            .expires(true)
                            .expirationDateFrom(OffsetDateTime.parse("2016-02-01T12:00:00.000-04:00"))
                            .expirationDateTo(OffsetDateTime.parse("2016-02-28T12:00:00.000-04:00"))
                            .build();
                    
                        return Mono.fromCallable(() -> {
                            try {
                                PreferenceClient client = new PreferenceClient();
                                Preference preference = client.create(preferenceRequest);
                    
                                // Logging de depuración
                                System.out.println("Preference created with ID: " + preference.getId());
                                System.out.println("Init point: " + preference.getInitPoint());
                                System.out.println("Sandbox init point: " + preference.getSandboxInitPoint());
                    
                                return preference;
                            } catch (MPException | MPApiException e) {
                                e.printStackTrace();
                                throw new RuntimeException("Error creating Mercado Pago preference", e);
                            }
                        }).onErrorResume(e -> {
                            return Mono.error(new RuntimeException("Error processing payment: " + e.getMessage(), e));
                        });

                    
                    
                    });
        
    }
    


    public Mono<PurchaseEntity> getPurchaseWithDetails(UUID idPurchase){
        return purchaseRepository.findById(idPurchase)
                                .flatMap(
                                   purchase -> {
                                        return purchaseDetailService.getDetailsFromPurchaseWithProducts(idPurchase)
                                           .collectList()
                                           .map(details -> {
                                               purchase.addDetails(details);
                                               return purchase;
                                           });
                                   });
    }
}
