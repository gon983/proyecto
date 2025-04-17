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


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
import com.mercadopago.resources.payment.PaymentStatus;
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
import com.proyect.mvp.application.dtos.database.PurchaseToFollowDTO;
import com.proyect.mvp.application.dtos.requests.LocationIdDTO;
import com.proyect.mvp.application.dtos.requests.ReceivePurchaseDTO;
import com.proyect.mvp.application.dtos.response.LocationResponseDTO;
import com.proyect.mvp.application.dtos.response.PurchaseDetailDTO;
import com.proyect.mvp.application.dtos.response.PurchaseFollowDTO;
import com.proyect.mvp.domain.model.entities.Location;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseDetailStateEntity;
import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.model.entities.UserEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;
import com.proyect.mvp.infrastructure.config.EnvConfigLoader;
import com.proyect.mvp.infrastructure.exception.PurchaseDetailNotInPendingStateException;
import com.proyect.mvp.infrastructure.exception.PurchaseNotInPendingStateException;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PurchaseService {

    private final PurchaseDetailStateService purchaseDetailStateService;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStateService purchaseStateService;
    private final PurchaseDetailService purchaseDetailService;
    private final UserService userService;
    private final LocationService locationService;
   
 

    static String NOTIFICATION_URL = EnvConfigLoader.getNotificationUrl();
    static String ACCESS_TOKEN = EnvConfigLoader.getAccessToken();
    static String SUCCESS_URL = EnvConfigLoader.getSuccessUrl();

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseStateService purchaseStateService, PurchaseDetailService purchaseDetailService,
     UserService userService,  PurchaseDetailStateService purchaseDetailStateService, LocationService locationService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
        this.purchaseDetailService = purchaseDetailService;
        this.userService = userService;
        this.locationService = locationService;
       
 
        this.purchaseDetailStateService = purchaseDetailStateService;
    }

    public Mono<PurchaseEntity> createEmptyCart(UUID idUser) {
        return purchaseStateService.findByName("pending") // Buscar estado en la DB
            .flatMap(purchaseState -> { 
                PurchaseEntity purchase = PurchaseEntity.builder()
                    .fkUser(idUser)
                    .createdAt(LocalDateTime.now())
                    .fkCurrentState(purchaseState.getIdPurchaseState()) // Asignar estado encontrado
                    .build();
                return purchaseRepository.save(purchase); // Guardar compra
            });
    }

    public Mono<List<PurchaseFollowDTO>> getPurchaseUserWithDetailsAndLocation(UUID idUser) {
        // Primero obtenemos las últimas 5 compras que no estén en estado pendiente
        return obtenerUltimasCincoComprasUser(idUser)
                .flatMap(purchase -> {
                    // Obtenemos los detalles de la compra
                    Mono<List<PurchaseDetailDTO>> detailsMono = purchaseDetailService.getDetailsDTO(purchase.getIdPurchase());
                    
                    // Obtenemos la información de la ubicación
                    Mono<LocationResponseDTO> locationMono = locationService.getLocationById(purchase.getIdLocation());
                    
                    // Combinamos ambos resultados para construir el DTO final
                    return Mono.zip(detailsMono, locationMono)
                            .map(tuple -> {
                                List<PurchaseDetailDTO> details = tuple.getT1();
                                LocationResponseDTO location = tuple.getT2();
                                String estimatedTime = "24 horas";
                                
                                return PurchaseFollowDTO.builder()
                                        .purchaseId(purchase.getIdPurchase())
                                        .details(details)
                                        .status("confirmed")
                                        .location(location)
                                        .estimatedDeliveryTime(estimatedTime)
                                        .build();
                            });
                })
                .collectList();
    }

    public Flux<PurchaseToFollowDTO> obtenerUltimasCincoComprasUser(UUID idUser){
        return purchaseStateService.findByName("pending")
                                    .flatMapMany(state-> {
                                        return purchaseRepository.findLastFiveUserNotPending(idUser, state.getIdPurchaseState());
                                    });
    }

    public Mono<Void> putLocation(UUID idPurchase, LocationIdDTO dto){
        return purchaseRepository.updateLocation(idPurchase, dto.getLocationId());
    }

    public Mono<Void> deletePurchaseWhenBuying(UUID idPurchase){
       
        return purchaseRepository.findById(idPurchase)
                                        .flatMap(purchase ->{ 

                                            return purchaseStateService.isPurchaseInPending(purchase.getFkCurrentState())
                                                                              .flatMap(result ->
                                                                              {
                                                                               
                                                                                if(result){
                                                                                    return purchaseRepository.deleteById(idPurchase);

                                                                                }else{
                                                                                    return Mono.error(new PurchaseNotInPendingStateException(idPurchase));

                                                                                }
                                                                              });
                                        });
                                        

    }


    public Flux<PurchaseEntity> getAllConfirmedPurchasesWithDetails() {
        return purchaseStateService.findByName("confirmed")
            .flatMapMany(state -> 
                purchaseRepository.findByFkCurrentState(state.getIdPurchaseState())
                    .flatMap(this::enrichPurchase)
            );
    }
    
    private Mono<PurchaseEntity> enrichPurchase(PurchaseEntity purchase) {
        return Mono.zip(
                getPurchaseDetails(purchase.getIdPurchase()),
                getUserForPurchase(purchase.getFkUser()),
                getLocationForPurchase(purchase.getIdLocation())
            )
            .map(tuple -> {
                purchase.addDetails(tuple.getT1());
                purchase.setUser(tuple.getT2());
                purchase.setLocation(tuple.getT3());
                return purchase;
            });
    }
    
    private Mono<List<PurchaseDetailEntity>> getPurchaseDetails(UUID purchaseId) {
        return purchaseDetailService.getDetailsFromPurchase(purchaseId).collectList();
    }

    private Mono<UserEntity> getUserForPurchase(UUID idUser) {
        return userService.getUserById(idUser);

    }

    private Mono<Location> getLocationForPurchase(UUID idLocation){
        return locationService.getLocationEntityById(idLocation);
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
        System.out.println("Iniciando processPayment - purchaseId: " + purchaseId + ", userId: " + userId);
        System.out.println("Cantidad de detalles recibidos: " + (details != null ? details.size() : "null"));
        
        return userService.getUserById(userId)
                .doOnNext(user -> System.out.println("Usuario encontrado: " + user.getUsername()))
                .doOnError(error -> System.err.println("Error al obtener usuario: " + error.getMessage()))
                .flatMap(user -> {
                    try {
                        System.out.println("Configurando token de acceso de MercadoPago");
                        // Configuramos el token de acceso principal
                        MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
                        System.out.println("Token configurado exitosamente");
                    
                        // Creamos los items para la preferencia
                        System.out.println("Creando items para la preferencia de pago");
                        List<PreferenceItemRequest> items = new ArrayList<>();
                        try {
                            items = details.stream()
                                    .<PreferenceItemRequest> map(detail -> {
                                        System.out.println("Procesando detalle: " + detail.getIdPurchaseDetail() + ", producto: " + 
                                            (detail.getProduct() != null ? detail.getProduct().getName() : "null"));
                                        
                                        BigDecimal price = null;
                                        try {
                                            price = new BigDecimal(detail.calculatePrice());
                                            System.out.println("Precio calculado: " + price);
                                        } catch (Exception e) {
                                            System.err.println("Error al calcular precio: " + e.getMessage());
                                            e.printStackTrace();
                                        }
                                        
                                        return PreferenceItemRequest.builder()
                                                .id(detail.getIdPurchaseDetail())
                                                .title(detail.getProduct().getName())
                                                .currencyId("ARS")
                                                .pictureUrl("https://www.mercadopago.com/org-img/MP3/home/logomp3.gif")
                                                .description("Producto de " + detail.getProduct().getName())
                                                .categoryId("food")
                                                .quantity(1)
                                                .unitPrice(price)
                                                .build();
                                    })
                                    .collect(Collectors.toList());
                            System.out.println("Items creados correctamente: " + items.size());
                        } catch (Exception e) {
                            System.err.println("Error al crear items para la preferencia: " + e.getMessage());
                            e.printStackTrace();
                        }
                    
                        // Resto de la configuración de la preferencia...
                        System.out.println("Configurando información del pagador (payer)");
                        PreferencePayerRequest payer = null;
                        try {
                            payer = PreferencePayerRequest.builder()
                                    .email(user.getEmail())
                                    .build();
                            System.out.println("Información del pagador configurada: " + user.getEmail());
                        } catch (Exception e) {
                            System.err.println("Error al configurar payer: " + e.getMessage());
                            e.printStackTrace();
                        }
                    
                        System.out.println("Configurando URLs de retorno");
                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                .success(SUCCESS_URL)
                                .build();
                    
                        System.out.println("Configurando métodos de pago");
                        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                                .excludedPaymentMethods(List.of())
                                .excludedPaymentTypes(List.of())
                                .build();
                    
                        // Almacenamos la información de la compra en metadata
                        // System.out.println("Creando metadata con purchaseId: " + purchaseId);
                        // Map<String, Object> metadata = new HashMap<>();
                        // metadata.put("purchase_id", purchaseId.toString());
                    
                        // Creamos la solicitud de preferencia - IMPORTANTE: Esta variable debe ser final o efectivamente final
                        System.out.println("Construyendo objeto de preferencia");
                        final PreferenceRequest preferenceRequest;
                        try {
                            preferenceRequest = PreferenceRequest.builder()
                                    .items(items)
                                    .payer(payer)
                                    .backUrls(backUrls)
                                    .autoReturn("approved")
                                    .paymentMethods(paymentMethods)
                                    .notificationUrl(NOTIFICATION_URL)
                                    .statementDescriptor("MARKETPLACE")
                                    .externalReference("Purchase_" + purchaseId.toString())
                                    .expires(true)
                                    .expirationDateFrom(OffsetDateTime.now())
                                    .expirationDateTo(OffsetDateTime.now().plusDays(7))
                                    .build();
                            System.out.println("Objeto de preferencia construido exitosamente");
                        } catch (Exception e) {
                            System.err.println("Error al construir objeto de preferencia: " + e.getMessage());
                            e.printStackTrace();
                            return Mono.error(new RuntimeException("Error building preference request: " + e.getMessage(), e));
                        }
                    
                        System.out.println("Preparando llamada a API de MercadoPago");
                        return Mono.fromCallable(() -> {
                            System.out.println("Ejecutando llamada a API para crear preferencia");
                            try {
                                PreferenceClient client = new PreferenceClient();
                                System.out.println("Cliente creado, enviando solicitud...");
                                Preference preference = client.create(preferenceRequest);
                                System.out.println("Preferencia creada exitosamente. ID: " + preference.getId());
                                System.out.println("URL de pago: " + preference.getInitPoint());
                                return preference;
                            } catch (MPException e) {
                                System.err.println("Error MPException: " + e.getMessage());
                                e.printStackTrace();
                                throw new RuntimeException("Error creating Mercado Pago preference (MPException)", e);
                            } catch (MPApiException e) {
                                System.err.println("Error MPApiException - Status: " + e.getStatusCode());
                                System.err.println("Response: " + (e.getApiResponse() != null ? e.getApiResponse().getContent() : "null"));
                                e.printStackTrace();
                                throw new RuntimeException("Error creating Mercado Pago preference (MPApiException)", e);
                            } catch (Exception e) {
                                System.err.println("Error general inesperado: " + e.getMessage());
                                e.printStackTrace();
                                throw new RuntimeException("Unexpected error creating Mercado Pago preference", e);
                            }
                        }).flatMap(preference -> {
                            System.out.println("Actualizando compra con ID de preferencia: " + preference.getId());
                            return updatePurchaseWithPreferenceId(purchaseId, preference.getId(), preference.getExternalReference())
                               .doOnSuccess(v -> System.out.println("Compra actualizada correctamente con preferenceId"))
                               .doOnError(e -> System.err.println("Error al actualizar compra: " + e.getMessage()))
                               .thenReturn(preference);
                        }).onErrorResume(e -> {
                            System.err.println("Error en el flujo de processPayment: " + e.getMessage());
                            if (e.getCause() != null) {
                                System.err.println("Causa: " + e.getCause().getMessage());
                            }
                            e.printStackTrace();
                            return Mono.<Preference>error(new RuntimeException("Error processing payment: " + e.getMessage(), e));
                        });
                    } catch (Exception e) {
                        System.err.println("Error general en processPayment: " + e.getMessage());
                        e.printStackTrace();
                        return Mono.<Preference>error(new RuntimeException("General error in processPayment: " + e.getMessage(), e));
                    }
                });    }
    
    private Mono<Void> updatePurchaseWithPreferenceId(UUID purchaseId, String preferenceId, String externalReference) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> {
                purchase.setMpPreferenceId(preferenceId);
                purchase.setExternalReference(externalReference);
                return purchaseRepository.save(purchase);
            })
            .then();
    }
    
    public Mono<Void> procesarNotificacionPago(String notificationType, String dataId) {
        System.out.println("=========== INICIO procesarNotificacionPago ===========");
        System.out.println("Parámetros recibidos: notificationType=" + notificationType + ", dataId=" + dataId);
    
        if (!"payment".equals(notificationType)) {
            System.out.println("Tipo de notificación no procesable: " + notificationType);
            System.out.println("=========== FIN procesarNotificacionPago (sin procesar) ===========");
            return Mono.empty();
        }
    
        System.out.println("Tipo de notificación correcto: payment");
        
        return Mono.defer(() -> {
            System.out.println("=========== INICIO defer ===========");
            try {
                System.out.println("Configurando token de acceso");
                MercadoPagoConfig.setAccessToken(ACCESS_TOKEN);
                System.out.println("Token configurado correctamente");
    
                System.out.println("Creando PaymentClient");
                PaymentClient paymentClient = new PaymentClient();
                System.out.println("PaymentClient creado correctamente");
    
                System.out.println("Intentando convertir dataId a Long: " + dataId);
                Long paymentId = Long.parseLong(dataId);
                System.out.println("Conversión exitosa, obteniendo datos del pago: " + paymentId);
    
                
                Payment payment = paymentClient.get(paymentId);
                
    
                if (payment.getId() == null) {
                    System.out.println("ADVERTENCIA: Id es NULL");
                    return Mono.empty();
                }
    
                if (!"approved".equals(payment.getStatus())) {
                    System.out.println("Pago NO aprobado, estado: " + payment.getStatus());
                    return Mono.empty();
                }
    
                String externalReference = payment.getExternalReference();
                // Eliminar el prefijo "Purchase_" si lo has configurado así
                String purchaseIdStr = externalReference.replace("Purchase_", "");
                UUID purchaseId = UUID.fromString(purchaseIdStr);
    
                System.out.println("Llamando a procesarPagoAprobado...");
                return purchaseRepository.findById(purchaseId)
                                        .flatMap(purchase -> {
                                            String preferenceId = purchase.getMpPreferenceId();
                                            System.out.println("PreferenceId obtenido de la base de datos: " + preferenceId);
                                            return procesarPagoAprobado(preferenceId, payment);
                                        });
    
            } catch (NumberFormatException e) {
                System.err.println("ERROR: No se pudo convertir dataId a Long: " + e.getMessage());
                e.printStackTrace();
                return Mono.error(new RuntimeException("Error al convertir dataId a Long", e));
            } catch (Exception e) {
                System.err.println("ERROR inesperado: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                return Mono.error(new RuntimeException("Error inesperado procesando notificación de pago", e));
            } finally {
                System.out.println("=========== FIN defer ===========");
            }
        })
        .doOnSuccess(v -> System.out.println("Procesamiento completado exitosamente"))
        .doOnError(e -> System.err.println("Error en el procesamiento reactivo: " + e.getMessage()))
        .doFinally(signal -> System.out.println("=========== FIN procesarNotificacionPago con señal: " + signal + " ==========="));
    }
    
    private Mono<Void> procesarPagoAprobado(String preferenceId, Payment payment) {
        System.out.println("Iniciando procesamiento de pago para preferenceId: " + preferenceId);
        
        return findByMpPreferenceId(preferenceId)
                .flatMap(purchase -> {
                    System.out.println("Compra encontrada: " + purchase.getIdPurchase());
                    
                    // Buscar el estado 'confirmed'
                    return purchaseStateService.findByName("confirmed")
                            .flatMap(state -> {
                                System.out.println("Estado 'confirmed' encontrado: " + state.getIdPurchaseState());
    
                                // Actualizar la compra
                                purchase.setFkCurrentState(state.getIdPurchaseState());
                                purchase.setMpPaymentId(payment.getId().toString());
                                purchase.setPaymentDate(payment.getDateApproved());
    
                                System.out.println("Actualizando compra con nuevos datos:");
                                System.out.println("- Nuevo estado: " + state.getIdPurchaseState());
                                System.out.println("- ID de pago MP: " + payment.getId());
                                System.out.println("- Fecha de pago: " + payment.getDateApproved());
                                
                                return purchaseRepository.save(purchase)
                                        .doOnSuccess(savedPurchase -> 
                                            System.out.println("Compra guardada exitosamente: " + savedPurchase.getIdPurchase()))
                                        .then(registrarVentaDeDetalles(purchase.getDetails())
                                            .doOnSuccess(v -> 
                                                System.out.println("Detalles de venta registrados exitosamente"))); 
                            });
                })
                .doOnError(error -> 
                    System.err.println("Error en el procesamiento del pago: " + error.getMessage()));
    }          
    

    private Mono<PurchaseEntity> findByMpPreferenceId(String idPreference){
        return purchaseRepository.findByMpPreferenceId(idPreference)
                                .flatMap(purchase -> {
                                        System.out.println("Compra encontrada: " + purchase.getIdPurchase());

                                        // Obtener detalles de la compra
                                        return purchaseDetailService.getDetailsFromPurchaseWithProducts(purchase.getIdPurchase())
                                                .collectList()
                                                .flatMap(details -> {
                                                    System.out.println("Detalles encontrados: " + details.size());
                                                    purchase.addDetails(details);
                                                    return Mono.just(purchase);
                                                });
        });

    }
    

  

    private Mono<Void> registrarVentaDeDetalles(List<PurchaseDetailEntity> details) {
    return purchaseDetailStateService.findByName("confirmed")
                                     .flatMapMany(state -> 
                                        Flux.fromIterable(details)
                                            .flatMap(detail -> {
                                                detail.setFkCurrentState(state.getIdPurchaseDetailState());
                                                return purchaseDetailService.save(detail);
                                            })
                                     )
                                     .then(); // Convierte Flux<Void> a Mono<Void>
}

    public Mono<List<PurchaseDetailEntity>> receivePurchase(UUID idPurchase, ReceivePurchaseDTO listReceived) {
        return purchaseDetailService.getDetailsFromPurchase(idPurchase)
                .flatMap(detail -> {
                    if (listReceived.contains(UUID.fromString(detail.getIdPurchaseDetail()))) {
                        return purchaseDetailStateService.findByName("received")
                                .map(state -> {
                                    detail.setFkCurrentState(state.getIdPurchaseDetailState());
                                    return detail;
                                });
                    } else {
                        return purchaseDetailStateService.findByName("not received")
                                .map(state -> {
                                    detail.setFkCurrentState(state.getIdPurchaseDetailState());
                                    return detail;
                                });
                    }
                })
                .collectList();
    }  


    public Mono<PurchaseEntity> getUserActiveCart(UUID userId) {
        return purchaseStateService.findByName("pending")
            .flatMap(pendingState -> 
                purchaseRepository.findByFkUserAndFkCurrentState(userId, pendingState.getIdPurchaseState())
                    .switchIfEmpty(
                        // Si no existe un carrito pendiente, crear uno nuevo
                        Mono.defer(() -> {
                            
                            return createEmptyCart(userId);
                        })
                    )
            )
            .flatMap(purchase -> 
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchase.getIdPurchase())
                    .collectList()
                    .map(details -> {
                        purchase.addDetails(details);
                        return purchase;
                    })
            );
    }
    
}
