package com.proyect.mvp.application.services;

import org.springframework.stereotype.Service;

import com.proyect.mvp.domain.repository.PurchaseRepository;
import com.proyect.mvp.application.dtos.create.PurchaseCreateDTO;
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.model.entities.StockMovementEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;

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
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class MercadoPagoSplitService {

    private final UserService userService;
    private final SaleService saleService;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseStateService purchaseStateService;
    private final PurchaseDetailService purchaseDetailService;

    // ================ CONFIGURACIÓN REQUERIDA =================
    private static final String APP_ID = "2552125444382264"; // Obtenido al crear la aplicación
    private static final String CLIENT_SECRET = "6jNUiU2UsjlkXaUzWcwXaqak40NhMEZ8"; // Obtenido al crear la aplicación
    private static final String MARKETPLACE_ACCESS_TOKEN = "APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108"; // Access Token del marketplace
    private static final String REDIRECT_URI = "https://662b-196-32-67-187.ngrok-free.app/mp/auth/callback"; // URL configurada en tu aplicación
    private static final String NOTIFICATION_URL = "https://662b-196-32-67-187.ngrok-free.app/mp/notification"; // URL para notificaciones
    // =========================================================

    public MercadoPagoSplitService(
            UserService userService, 
            SaleService saleService, 
            PurchaseRepository purchaseRepository,
            PurchaseStateService purchaseStateService,
            PurchaseDetailService purchaseDetailService) {
        this.userService = userService;
        this.saleService = saleService;
        this.purchaseRepository = purchaseRepository;
        this.purchaseStateService = purchaseStateService;
        this.purchaseDetailService = purchaseDetailService;
    }

    public String generarUrlAutorizacionProductor(UUID productorId) {
        try {
            // Genera la URL de autorización
            return "https://auth.mercadopago.com/authorization?client_id="+ APP_ID
            +"&response_type=code&platform_id=mp&state="+ productorId
            +"&redirect_uri=" + REDIRECT_URI;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la URL de autorización", e);
        }
    }        
        
    

    /**
     * Procesa el código de autorización recibido cuando un productor autoriza a tu marketplace
     * Debe ser llamado desde el endpoint configurado en REDIRECT_URI
     */
    public Mono<Void> procesarAutorizacionProductor(String code, UUID productorId) {
        return WebClient.create("https://api.mercadopago.com")
            .post()
            .uri("/oauth/token")
            .header("accept", "application/json")
            .header("content-type", "application/x-www-form-urlencoded")
            .body(BodyInserters.fromFormData("client_id", APP_ID)
                .with("client_secret", CLIENT_SECRET)
                .with("grant_type", "authorization_code")
                .with("code", code)
                .with("redirect_uri", REDIRECT_URI)
                .with("state", productorId.toString()))
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(response -> {
                // Parsear la respuesta
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                String accessToken = jsonResponse.get("access_token").getAsString();
                String userId = jsonResponse.get("user_id").getAsString();
                String refreshToken = jsonResponse.get("refresh_token").getAsString();
                
                System.out.println("Autenticación exitosa para productor: " + productorId);
                System.out.println("MP User ID: " + userId);
                System.out.println("MP access token: " + accessToken);
                System.out.println("MP refresh token : " + refreshToken);
                
                
                // Guardar los datos del productor en la base de datos
                return userService.saveProducerMpData(productorId, accessToken, userId, refreshToken);
            });
    }

    /**
     * Crea una preferencia de pago para una compra
     * Esta preferencia mostrará al usuario la página de pago de Mercado Pago
     */
    public Mono<Preference> crearPreferenciaPago(UUID purchaseId) {
        System.out.println("Creando preferencia de pago para compra: " + purchaseId);
        
        // Configurar el token del marketplace
        MercadoPagoConfig.setAccessToken(MARKETPLACE_ACCESS_TOKEN);
        
        return purchaseRepository.findById(purchaseId)
            .switchIfEmpty(Mono.error(new RuntimeException("Compra no encontrada")))
            .flatMap(purchase -> 
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchaseId)
                    .collectList()
                    .flatMap(details -> {
                        if (details.isEmpty()) {
                            return Mono.error(new RuntimeException("La compra no tiene detalles"));
                        }
                        
                        return userService.getUserById(purchase.getFkUser())
                            .flatMap(user -> {
                                try {
                                    // Crear los items para la preferencia
                                    List<PreferenceItemRequest> items = details.stream()
                                        .map(detail -> {
                                            BigDecimal price = new BigDecimal(detail.calculatePrice());
                                            return PreferenceItemRequest.builder()
                                                .id(detail.getIdPurchaseDetail().toString())
                                                .title(detail.getProduct().getName())
                                                .description("Producto: " + detail.getProduct().getName())
                                                .categoryId("marketplace_items")
                                                .quantity(1)
                                                .currencyId("ARS")
                                                .unitPrice(price)
                                                .build();
                                        })
                                        .collect(Collectors.toList());
                                        
                                    // Crear información del comprador
                                    PreferencePayerRequest payer = PreferencePayerRequest.builder()
                                        .name(user.getFirstName())
                                        .surname(user.getLastName())
                                        .email(user.getEmail())
                                        .phone(PhoneRequest.builder().areaCode("54").number(user.getPhone()).build())
                                        .identification(IdentificationRequest.builder().type(user.getDocumentType()).number(user.getDocumentNumber()).build())
                                        .address(AddressRequest.builder().streetName("Street").zipCode("06233200").build())
                                        .build();
                                    
                                    // Configurar URLs de retorno
                                    PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                        .success("https://tu-dominio.com/pago-exitoso")
                                        .failure("https://tu-dominio.com/pago-fallido")
                                        .pending("https://tu-dominio.com/pago-pendiente")
                                        .build();
                                    
                                    // Configurar métodos de pago
                                    PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                                        .excludedPaymentTypes(List.of()) // Puedes excluir tipos de pago si lo deseas
                                        .installments(12) // Cantidad máxima de cuotas
                                        .build();
                                    
                                    // Construir la solicitud de preferencia completa
                                    PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                        .items(items)
                                        .payer(payer)
                                        .backUrls(backUrls)
                                        .notificationUrl(NOTIFICATION_URL)
                                        .externalReference("Purchase_" + purchaseId)
                                        .autoReturn("approved")
                                        .paymentMethods(paymentMethods)
                                        .expires(true)
                                        .expirationDateFrom(OffsetDateTime.now())
                                        .expirationDateTo(OffsetDateTime.now().plusDays(2))
                                        .build();
                                    
                                    System.out.println("Solicitud de preferencia creada, enviando a Mercado Pago...");
                                    
                                    // Crear la preferencia en Mercado Pago
                                    return Mono.fromCallable(() -> {
                                        PreferenceClient client = new PreferenceClient();
                                        return client.create(preferenceRequest);
                                    });
                                    
                                } catch (Exception e) {
                                    System.err.println("Error al crear preferencia: " + e.getMessage());
                                    e.printStackTrace();
                                    return Mono.error(new RuntimeException("Error al crear preferencia", e));
                                }
                            });
                    })
                    .flatMap(preference -> {
                        // Guardar el ID de la preferencia en la compra
                        return updatePurchaseWithPreferenceId(purchaseId, preference.getId(), preference.getExternalReference())
                            .thenReturn(preference);
                    })
            );
    }
    
    /**
     * Actualiza la compra con el ID de la preferencia generada
     */
    private Mono<Void> updatePurchaseWithPreferenceId(UUID purchaseId, String preferenceId, String externalReference) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> {
                purchase.setMpPreferenceId(preferenceId);
                purchase.setExternalReference(externalReference);
                return purchaseRepository.save(purchase);
            })
            .then();
    }

    /**
     * Procesa una notificación de pago recibida de Mercado Pago
     */
    public Mono<Void> procesarNotificacionPago(String notificationType, String dataId) {
        System.out.println("=========== INICIO procesarNotificacionPago ===========");
        System.out.println("Parámetros recibidos: notificationType=" + notificationType + ", dataId=" + dataId);
    
        if (!"payment".equals(notificationType)) {
            System.out.println("Tipo de notificación no procesable: " + notificationType);
            return Mono.empty();
        }
    
        System.out.println("Tipo de notificación correcto: payment");
        
        return Mono.defer(() -> {
            try {
                System.out.println("Configurando token de acceso");
                MercadoPagoConfig.setAccessToken(MARKETPLACE_ACCESS_TOKEN);
                
                System.out.println("Creando PaymentClient");
                PaymentClient paymentClient = new PaymentClient();
                
                System.out.println("Obteniendo datos del pago: " + dataId);
                Long paymentId = Long.parseLong(dataId);
                Payment payment = paymentClient.get(paymentId);
                
                if (payment.getId() == null) {
                    System.out.println("ADVERTENCIA: Id de pago es NULL");
                    return Mono.empty();
                }
    
                if (!"approved".equals(payment.getStatus())) {
                    System.out.println("Pago NO aprobado, estado: " + payment.getStatus());
                    return Mono.empty();
                }
    
                String externalReference = payment.getExternalReference();
                String purchaseIdStr = externalReference.replace("Purchase_", "");
                UUID purchaseId = UUID.fromString(purchaseIdStr);
    
                System.out.println("Procesando pago aprobado para compra: " + purchaseId);
                return procesarPagoAprobado(purchaseId, payment);
            } catch (Exception e) {
                System.err.println("ERROR en procesarNotificacionPago: " + e.getMessage());
                e.printStackTrace();
                return Mono.error(new RuntimeException("Error procesando notificación", e));
            }
        });
    }
    
    /**
     * Procesa un pago aprobado
     */
    private Mono<Void> procesarPagoAprobado(UUID purchaseId, Payment payment) {
        return purchaseRepository.findById(purchaseId)
            .switchIfEmpty(Mono.error(new RuntimeException("Compra no encontrada para ID: " + purchaseId)))
            .flatMap(purchase -> {
                // Obtener el estado "confirmed"
                return purchaseStateService.findByName("confirmed")
                    .switchIfEmpty(Mono.error(new RuntimeException("Estado 'confirmed' no encontrado")))
                    .flatMap(state -> {
                        // Actualizar la compra
                        purchase.setFkCurrentState(state.getIdPurchaseState());
                        purchase.setMpPaymentId(payment.getId().toString());
                        purchase.setPaymentDate(payment.getDateApproved());
                        
                        return purchaseRepository.save(purchase);
                    });
            })
            .flatMap(purchase -> 
                // Obtener los detalles de la compra
                purchaseDetailService.getDetailsFromPurchaseWithProducts(purchase.getIdPurchase())
                    .collectList()
                    .flatMap(details -> {
                        // Registrar ventas y procesar pagos
                        return procesarPagosProductores(details, purchase, payment);
                    })
            )
            .then();
    }
    
    /**
     * Método principal para procesar un pago con split
     * Este método se llama después de que el pago ha sido aprobado
     */
    private Mono<Void> procesarPagosProductores(List<PurchaseDetailEntity> details, PurchaseEntity purchase, Payment payment) {
        System.out.println("=========== INICIO procesarPagosProductores ===========");
        System.out.println("Detalles recibidos: " + details.size() + ", paymentId: " + payment.getId());
        
        // Configurar el token del marketplace
        MercadoPagoConfig.setAccessToken(MARKETPLACE_ACCESS_TOKEN);
        
        // Agrupamos por productor
        Map<UUID, List<PurchaseDetailEntity>> detallesPorProductor = details.stream()
            .filter(detail -> detail.getProduct() != null && detail.getProduct().getFkProductor() != null)
            .collect(Collectors.groupingBy(detail -> detail.getProduct().getFkProductor()));
        
        System.out.println("Total de productores a pagar: " + detallesPorProductor.size());
        
        // Para cada productor, registrar ventas y procesar pagos
        return Flux.fromIterable(detallesPorProductor.entrySet())
            .flatMap(entry -> {
                UUID productorId = entry.getKey();
                List<PurchaseDetailEntity> detallesProductor = entry.getValue();
                
                System.out.println("Procesando productor: " + productorId + " con " + detallesProductor.size() + " productos");
                
                // Registrar ventas para este productor
                return Flux.fromIterable(detallesProductor)
                    .flatMap(detail -> 
                        saleService.registrarVenta(detail.getIdPurchaseDetail(), productorId)
                    )
                    .collectList()
                    .flatMap(ventas -> {
                        // Calcular el monto total para este productor
                        BigDecimal montoProductor = detallesProductor.stream()
                            .map(detail -> new BigDecimal(detail.calculatePrice()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                        
                        // Crear un pago split para este productor
                        return crearPagoSplit(productorId, montoProductor, payment.getId());
                    });
            })
            .collectList()
            .then();
    }
    
    /**
     * Crea un pago split para un productor específico
     */
    private Mono<String> crearPagoSplit(UUID productorId, BigDecimal monto, Long paymentId) {
        System.out.println("Creando pago split para productor: " + productorId + ", monto: " + monto);
        
        return userService.getMpProductorUserId(productorId)
            .doOnNext(userId -> System.out.println(userId))
            .flatMap(mpUserId -> {
                // Crear el objeto de pago
                JsonObject pagoSplit = new JsonObject();
                pagoSplit.addProperty("payment_id", paymentId);
                
                // Calcular comisión del marketplace (ejemplo: 5%)
                BigDecimal comision = monto.multiply(new BigDecimal("0.05"));
                BigDecimal montoNeto = monto.subtract(comision);
                
                pagoSplit.addProperty("amount", montoNeto);
                pagoSplit.addProperty("collector_id", mpUserId);
                
                // Aplicar comisión del marketplace
                pagoSplit.addProperty("application_fee", comision);
                
                System.out.println("Solicitud de pago split: " + pagoSplit);
                
                // Enviar la solicitud a Mercado Pago
                return WebClient.create("https://api.mercadopago.com")
                    .post()
                    .uri("/v1/payments/" + paymentId + "/disbursements")
                    .header("Authorization", "Bearer " + MARKETPLACE_ACCESS_TOKEN)
                    .header("Content-Type", "application/json")
                    .bodyValue(pagoSplit.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> {
                        System.out.println("Pago split creado exitosamente: " + response);
                    })
                    .doOnError(e -> {
                        System.err.println("Error al crear pago split: " + e.getMessage());
                    });
            });
    }    
    /**
     * Crea una compra completa con pagos split (método principal)
     * Este método combina todo el flujo de compra, desde crear la compra hasta generar la preferencia
     */
    // public Mono<Preference> crearCompraCompleta(PurchaseCreateDTO purchaseDto, List<PurchaseDetailEntity> details) {
    //     System.out.println("Creando compra completa con pagos split");
    
    //     // 1. Crear la compra
    //     return purchaseStateService.findByName("pending")
    //         .switchIfEmpty(Mono.error(new IllegalStateException("Estado 'pending' no encontrado")))
    //         .flatMap(purchaseState -> {
    //             PurchaseEntity purchase = PurchaseEntity.builder()
    //                 .idPurchase(UUID.randomUUID())
    //                 .fkUser(purchaseDto.getFkUser())
    //                 .level(purchaseDto.getLevel())
    //                 .createdAt(LocalDateTime.now())
    //                 .fkCurrentState(purchaseState.getIdPurchaseState())
    //                 .build();
    
    //             return purchaseRepository.save(purchase);
    //         })
    //         .flatMap(purchase -> {
    //             // 2. Guardar los detalles de la compra
    //             UUID purchaseId = purchase.getIdPurchase();
    //             return Flux.fromIterable(details)
    //                 .flatMap(detail -> {
    //                     detail.setFkPurchase(purchaseId);
    //                     return purchaseDetailService.save(detail);
    //                 })
    //                 .then(Mono.just(purchaseId));  // Use `then` to return a Mono<UUID>
    //         })
    //         .flatMap(purchaseId -> {
    //             // 3. Crear la preferencia de pago (explicitly return Mono<Preference>)
    //             return crearPreferenciaPago(purchaseId);
    //         });
    // }

    

    /**
     * Método para refrescar el token de un productor cuando expire
     */
    public Mono<String> refrescarTokenProductor(UUID productorId) {
        return userService.getMpRefreshToken(productorId)
            .flatMap(refreshToken -> {
                return WebClient.create("https://api.mercadopago.com")
                    .post()
                    .uri("/oauth/token")
                    .header("accept", "application/json")
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body(BodyInserters.fromFormData("client_id", APP_ID)
                        .with("client_secret", CLIENT_SECRET)
                        .with("grant_type", "refresh_token")
                        .with("refresh_token", refreshToken))
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(response -> {
                        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                        String newAccessToken = jsonResponse.get("access_token").getAsString();
                        String newRefreshToken = jsonResponse.get("refresh_token").getAsString();
                        
                        // Guardar los nuevos tokens y devolver el access token
                        return userService.updateProducerMpTokens(productorId, newAccessToken, newRefreshToken)
                            .thenReturn(newAccessToken); // Use thenReturn instead of then(Mono.just())
                    });
            });
    }
}