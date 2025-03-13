package com.proyect.mvp.application.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.common.AddressRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.client.oauth.OauthClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
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
import com.proyect.mvp.domain.model.entities.StockMovementEntity;
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
    private final StockMovementService stockMovementService;
    

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseStateService purchaseStateService, PurchaseDetailService purchaseDetailService,
     UserService userService, EncryptionService encryptionService, StockMovementService stockMovementService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
        this.purchaseDetailService = purchaseDetailService;
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.stockMovementService = stockMovementService;
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


    public Flux<Preference> confirmPurchase(UUID purchaseId) {
        return purchaseRepository.findById(purchaseId)
            .flatMapMany(purchase -> 
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchaseId)
                    .collectList()
                    .flatMap(details -> processPayment(details, purchase.getFkUser(), purchaseId))
                    .flux()
            );
    }
    
    private Mono<Preference> processPayment(List<PurchaseDetailEntity> details, UUID userId, UUID purchaseId) {
            return userService.getUserById(userId)
                    .flatMap(user -> {
                        // Configuramos el token de acceso principal
                        MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
                    
                        // Creamos los items para la preferencia
                        List<PreferenceItemRequest> items = details.stream()
                                .map(detail -> PreferenceItemRequest.builder()
                                        .id(detail.getIdPurchaseDetail())
                                        .title(detail.getProduct().getName())
                                        .currencyId("ARS")
                                        .pictureUrl("https://www.mercadopago.com/org-img/MP3/home/logomp3.gif")
                                        .description("Producto de " + detail.getProduct().getName())
                                        .categoryId("food")
                                        .quantity(1)
                                        .unitPrice(new BigDecimal(detail.calculatePrice()))
                                        .build())
                                .collect(Collectors.toList());
                    
                        // Resto de la configuración de la preferencia...
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
                    
                        // Almacenamos la información de la compra en metadata
                        Map<String, Object> metadata = new HashMap<>();
                        metadata.put("purchase_id", purchaseId.toString());
                    
                        // Creamos la solicitud de preferencia
                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                .items(items)
                                .payer(payer)
                                .backUrls(backUrls)
                                .autoReturn("approved")
                                .paymentMethods(paymentMethods)
                                .notificationUrl("https://www.your-site.com/ipn")
                                .statementDescriptor("MARKETPLACE")
                                .externalReference("Purchase_" + purchaseId.toString())
                                .expires(true)
                                .expirationDateFrom(OffsetDateTime.now())
                                .expirationDateTo(OffsetDateTime.now().plusDays(7))
                                .metadata(metadata)
                                .build();
                    
                        return Mono.fromCallable(() -> {
                            try {
                                PreferenceClient client = new PreferenceClient();
                                Preference preference = client.create(preferenceRequest);
                                return preference;
                            } catch (MPException | MPApiException e) {
                                e.printStackTrace();
                                throw new RuntimeException("Error creating Mercado Pago preference", e);
                            }
                        }).flatMap(preference -> {
                            // Usamos thenReturn ya que updatePurchaseWithPreferenceId devuelve Mono<Void>
                            return updatePurchaseWithPreferenceId(purchaseId, preference.getId())
                               .thenReturn(preference);
                        }).onErrorResume(e -> {
                            // Especificamos el tipo de error como Preference
                            return Mono.<Preference>error(new RuntimeException("Error processing payment: " + e.getMessage(), e));
                        });
                    });
        }
    
    private Mono<Void> updatePurchaseWithPreferenceId(UUID purchaseId, String preferenceId) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> {
                purchase.setMpPreferenceId(preferenceId);
                return purchaseRepository.save(purchase);
            })
            .then();
    }
    
    // Método para procesar las notificaciones de pago
    public Mono<Void> procesarNotificacionPago(String notificationType, String dataId) {
        System.out.println("Método procesarNotificacionPago llamado con: " + notificationType + ", " + dataId);
        if ("payment".equals(notificationType)) {
            System.out.println("Tipo de notificación correcto: payment");
            return Mono.fromCallable(() -> {
                System.out.println("Dentro de fromCallable");
                try {
                    // Configuramos el token de acceso principal
                    MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
                    
                    // Obtenemos la información del pago
                    PaymentClient paymentClient = new PaymentClient();
                    Payment payment = paymentClient.get(Long.parseLong(dataId));
                    System.out.println("Payment recuperado: ID=" + payment.getId());
                    System.out.println("Payment status: " + payment.getStatus());
                    System.out.println("Payment externalReference: " + payment.getExternalReference());
                    
                    // Verificamos si el pago está aprobado
                    if ("approved".equals(payment.getStatus())) {
                        // Obtenemos el preferenceId
                        String preferenceId = payment.getExternalReference();
                        
                        // Buscamos la compra con ese preferenceId
                        return procesarPagoAprobado(preferenceId, payment);
                    }
                    
                    return null;
                } catch (MPException | MPApiException e) {
                    System.err.println("Error de MercadoPago: " + e.getClass().getName() + ": " + e.getMessage());
                    if (e instanceof MPApiException) {
                        MPApiException apiEx = (MPApiException) e;
                        System.err.println("Status Code: " + apiEx.getStatusCode());
                        System.err.println("API Response: " + apiEx.getApiResponse());
                    }
                    e.printStackTrace();
                    throw new RuntimeException("Error processing payment notification", e);
                }
            }).then();
        }
        
        return Mono.empty();
    }
    
    private Mono<Void> procesarPagoAprobado(String preferenceId, Payment payment) {
        return purchaseRepository.findByMpPreferenceId(preferenceId)
                        .flatMap(
                    purchase -> {
                        return purchaseDetailService.getDetailsFromPurchaseWithProducts(purchase.getIdPurchase())
                                        .collectList()
                                        .flatMap(details -> {
                                            purchase.addDetails(details);
                                            return Mono.just(purchase);
                                        });
                    })
            .flatMap(purchase -> {
                // Marcar la compra como pagada
                return purchaseStateService.findByName("confirmed")
                    .flatMap(state -> {
                        purchase.setFkCurrentState(state.getIdPurchaseState());
                        purchase.setMpPaymentId(payment.getId().toString());
                        purchase.setPaymentDate(payment.getDateApproved());

                        
                        
                        

                        return purchaseRepository.save(purchase)
                            .flatMap(updatedPurchase -> {
                                // Ahora procesamos los pagos a los productores
                                return purchaseDetailService.getDetailsFromPurchaseWithProducts(updatedPurchase.getIdPurchase())
                                    .collectList()
                                    .flatMap(details -> procesarPagosProductores(details, payment));
                            });
                    });
            })
            .then();
    }

   
    
    private Mono<Void> procesarPagosProductores(List<PurchaseDetailEntity> details, Payment payment) {
        // Agrupamos por productor
        Map<UUID, List<PurchaseDetailEntity>> detallesPorProductor = details.stream()
                .collect(Collectors.groupingBy(detail -> detail.getProduct().getFkProductor()));
    
        // Procesamos cada productor
        return Flux.fromIterable(detallesPorProductor.entrySet())
            .flatMap((Entry<UUID, List<PurchaseDetailEntity>> entry) -> {
                UUID productorId = entry.getKey();
                List<PurchaseDetailEntity> detallesProductor = entry.getValue();
    
                BigDecimal montoProductor = detallesProductor.stream()
                        .map(detail -> new BigDecimal(detail.calculatePrice()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
                return userService.getUserById(productorId)
                    .flatMap(productor -> transferirFondosAProductor(productorId, productor.getMpAccessToken(), montoProductor, payment.getId()))
                    .then(Mono.empty()); // Explicita el tipo de retorno como Mono<Void>
            })
            .then();
    }
    

   
    
    private Mono<Object> transferirFondosAProductor(UUID productorId, String mpAccessToken, BigDecimal monto, Long paymentId) {
        return Mono.defer(() -> {
            try {
                // Temporalmente cambiamos al token del productor
                MercadoPagoConfig.setAccessToken(mpAccessToken);
                
                // Construimos la solicitud de pago (Payout)
                JsonObject payoutRequest = new JsonObject();
                payoutRequest.addProperty("amount", monto);
                payoutRequest.addProperty("currency_id", "ARS"); // Cambiar si se usa otra moneda
                payoutRequest.addProperty("description", "Pago de venta ID " + paymentId);
                
                // Enviamos la solicitud a la API de Mercado Pago
                HttpResponse<String> response = Unirest.post("https://api.mercadopago.com/v1/payments")
                    .header("Authorization", "Bearer " + mpAccessToken)
                    .header("Content-Type", "application/json")
                    .body(payoutRequest.toString())
                    .asString();
                
                // Verificamos la respuesta
                if (response.getStatus() >= 200 && response.getStatus() < 300) {
                    System.out.println("Transferidos " + monto + " al productor " + productorId);
                    return Mono.empty();
                } else {
                    System.err.println("Error en la transferencia: " + response.getBody());
                    return Mono.error(new RuntimeException("Error en la transferencia: " + response.getBody()));
                }
            } catch (Exception e) {
                System.err.println("Error al transferir fondos al productor " + productorId + ": " + e.getMessage());
                return Mono.error(e);
            } finally {
                // Restauramos el token principal
                MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
            }
        }).onErrorResume(e -> {
            System.err.println("Error procesando pago para productor " + productorId + ": " + e.getMessage());
            return Mono.empty();
        });
    }
    
    
}
