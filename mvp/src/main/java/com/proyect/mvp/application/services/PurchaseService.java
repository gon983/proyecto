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
import com.proyect.mvp.domain.model.entities.PurchaseDetailEntity;
import com.proyect.mvp.domain.model.entities.PurchaseEntity;
import com.proyect.mvp.domain.model.entities.PurchaseStateEntity;
import com.proyect.mvp.domain.model.entities.StockMovementEntity;
import com.proyect.mvp.domain.repository.PurchaseRepository;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
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
        System.out.println("Iniciando processPayment - purchaseId: " + purchaseId + ", userId: " + userId);
        System.out.println("Cantidad de detalles recibidos: " + (details != null ? details.size() : "null"));
        
        return userService.getUserById(userId)
                .doOnNext(user -> System.out.println("Usuario encontrado: " + user.getFirstName() + " " + user.getLastName() + " (ID: " + user.getIdUser() + ")"))
                .doOnError(error -> System.err.println("Error al obtener usuario: " + error.getMessage()))
                .flatMap(user -> {
                    try {
                        System.out.println("Configurando token de acceso de MercadoPago");
                        // Configuramos el token de acceso principal
                        MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
                        System.out.println("Token configurado exitosamente");
                    
                        // Creamos los items para la preferencia
                        System.out.println("Creando items para la preferencia de pago");
                        List<PreferenceItemRequest> items = new ArrayList<>();
                        try {
                            items = details.stream()
                                    .map(detail -> {
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
                                    .name(user.getFirstName())
                                    .surname(user.getLastName())
                                    .email(user.getEmail())
                                    .phone(PhoneRequest.builder().areaCode("54").number(user.getPhone()).build())
                                    .identification(IdentificationRequest.builder().type(user.getDocumentType()).number(user.getDocumentNumber()).build())
                                    .address(AddressRequest.builder().streetName("Street").zipCode("06233200").build())
                                    .build();
                            System.out.println("Información del pagador configurada: " + user.getEmail());
                        } catch (Exception e) {
                            System.err.println("Error al configurar payer: " + e.getMessage());
                            e.printStackTrace();
                        }
                    
                        System.out.println("Configurando URLs de retorno");
                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                .success("https://www.success.com")
                                .failure("http://www.failure.com")
                                .pending("http://www.pending.com")
                                .build();
                    
                        System.out.println("Configurando métodos de pago");
                        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                                .excludedPaymentMethods(List.of())
                                .excludedPaymentTypes(List.of())
                                .build();
                    
                        // Almacenamos la información de la compra en metadata
                        System.out.println("Creando metadata con purchaseId: " + purchaseId);
                        Map<String, Object> metadata = new HashMap<>();
                        metadata.put("purchase_id", purchaseId.toString());
                    
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
                                    .notificationUrl(" https://7068-196-32-67-187.ngrok-free.app/confirmPayment")
                                    .statementDescriptor("MARKETPLACE")
                                    .externalReference("Purchase_" + purchaseId.toString())
                                    .expires(true)
                                    .expirationDateFrom(OffsetDateTime.now())
                                    .expirationDateTo(OffsetDateTime.now().plusDays(7))
                                    .metadata(metadata)
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
                            // Especificamos el tipo de error como Preference
                            return Mono.<Preference>error(new RuntimeException("Error processing payment: " + e.getMessage(), e));
                        });
                    } catch (Exception e) {
                        System.err.println("Error general en processPayment: " + e.getMessage());
                        e.printStackTrace();
                        return Mono.<Preference>error(new RuntimeException("General error in processPayment: " + e.getMessage(), e));
                    }
                });
    }
    
    private Mono<Void> updatePurchaseWithPreferenceId(UUID purchaseId, String preferenceId, String externalReference) {
        return purchaseRepository.findById(purchaseId)
            .flatMap(purchase -> {
                purchase.setMpPreferenceId(preferenceId);
                purchase.setExternalReference(externalReference);
                return purchaseRepository.save(purchase);
            })
            .then();
    }
    
    public Mono<Void> procesarNotificacionPago(String notificationType, String dataId) throws MPException, MPApiException {
        System.out.println("=========== INICIO procesarNotificacionPago ===========");
        System.out.println("Parámetros recibidos: notificationType=" + notificationType + ", dataId=" + dataId);
    
        if ("payment".equals(notificationType)) {
            System.out.println("Tipo de notificación correcto: payment");
            return Mono.fromCallable(() -> {
                System.out.println("=========== INICIO fromCallable ===========");
                try {
                    System.out.println("Configurando token de acceso");
                    MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
                    System.out.println("Token configurado correctamente");
    
                    System.out.println("Creando PaymentClient");
                    PaymentClient paymentClient = new PaymentClient();
                    System.out.println("PaymentClient creado correctamente");
    
                    System.out.println("Intentando convertir dataId a Long: " + dataId);
                    Long paymentId = Long.parseLong(dataId);
                    System.out.println("Conversión exitosa, obteniendo datos del pago: " + paymentId);
    
                    // Simulación de un pago
                    SimuledPayment payment = new SimuledPayment();
                    payment.setId(paymentId);
                    payment.setStatus("approved");
                    payment.setDateApproved(OffsetDateTime.now());
    
                    if (payment.getId() == null) {
                        System.out.println("ADVERTENCIA: Id es NULL");
                    }
    
                    if ("approved".equals(payment.getStatus())) {
                        System.out.println("Pago aprobado, procesando...");
                        String preferenceId = "54615ae8-04f0-475f-af83-7132fe04884e"; // hay q hardcodearlo porque en realidad se obtiene desde el objeto payment 
                        System.out.println("PId obtenido: " + preferenceId);
    
                        System.out.println("Llamando a procesarPagoAprobado...");
                        Object resultado = procesarPagoAprobado(preferenceId, payment);
                        System.out.println("procesarPagoAprobado completado con resultado: " + resultado);
                        return resultado;
                    } else {
                        System.out.println("Pago NO aprobado, estado: " + payment.getStatus());
                    }
    
                    System.out.println("Retornando null ya que no se procesó el pago");
                    return null;
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: No se pudo convertir dataId a Long: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error al convertir dataId a Long", e);
                } catch (Exception e) {
                    System.err.println("ERROR inesperado: " + e.getClass().getName() + ": " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error inesperado procesando notificación de pago", e);
                } finally {
                    System.out.println("=========== FIN fromCallable ===========");
                }
            }).then()
              .doOnSuccess(v -> System.out.println("Procesamiento completado exitosamente"))
              .doOnError(e -> System.err.println("Error en el procesamiento reactivo: " + e.getMessage()))
              .doFinally(signal -> System.out.println("=========== FIN procesarNotificacionPago con señal: " + signal + " ==========="));
        } else {
            System.out.println("Tipo de notificación no procesable: " + notificationType);
            System.out.println("=========== FIN procesarNotificacionPago (sin procesar) ===========");
            return Mono.empty();
        }
    }
    
    private Mono<Void> procesarPagoAprobado(String preferenceId, SimuledPayment payment) {
        System.out.println("=========== INICIO procesarPagoAprobado ===========");
        System.out.println("Parámetros recibidos: preferenceId=" + preferenceId + ", paymentId=" + payment.getId());
    
        return purchaseRepository.findByMpPreferenceId(preferenceId)
                .doOnNext(purchase -> System.out.println("Compra encontrada con ID: " + purchase.getIdPurchase()))
                .doOnError(e -> System.err.println("ERROR: No se encontró la compra con preferenceId: " + preferenceId + " - " + e.getMessage()))
                .flatMap(purchase -> {
                    System.out.println("Obteniendo detalles de la compra: " + purchase.getIdPurchase());
                    return purchaseDetailService.getDetailsFromPurchaseWithProducts(purchase.getIdPurchase())
                            .doOnNext(detail -> System.out.println("- Detalle encontrado: " + detail.getIdPurchaseDetail() + ", producto: " +
                                    (detail.getProduct() != null ? detail.getProduct().getIdProduct() : "null")))
                            .collectList()
                            .doOnNext(details -> System.out.println("Total de detalles encontrados: " + details.size()))
                            .flatMap(details -> {
                                System.out.println("Añadiendo detalles a la compra");
                                purchase.addDetails(details);
                                return Mono.just(purchase);
                            });
                })
                .flatMap(purchase -> {
                    System.out.println("Buscando estado 'confirmed' para actualizar la compra");
                    return purchaseStateService.findByName("confirmed")
                        .doOnNext(state -> System.out.println("Estado 'confirmed' encontrado con ID: " + state.getIdPurchaseState()))
                        .doOnError(e -> System.err.println("ERROR: No se encontró el estado 'confirmed' - " + e.getMessage()))
                        .flatMap(state -> {
                            System.out.println("Actualizando compra con nuevo estado");
                            purchase.setFkCurrentState(state.getIdPurchaseState());
                            purchase.setMpPaymentId(payment.getId().toString());
                            purchase.setPaymentDate(payment.getDateApproved());
    
                            System.out.println("Datos actualizados: estado=" + state.getIdPurchaseState() +
                                    ", mpPaymentId=" + payment.getId() +
                                    ", paymentDate=" + payment.getDateApproved());
    
                            System.out.println("Guardando compra actualizada");
                            return purchaseRepository.save(purchase)
                                .doOnNext(updatedPurchase -> System.out.println("Compra guardada correctamente: " + updatedPurchase.getIdPurchase()))
                                .doOnError(e -> System.err.println("ERROR al guardar la compra: " + e.getMessage()))
                                .flatMap(updatedPurchase -> {
                                    System.out.println("Obteniendo detalles de la compra actualizada para procesar pagos");
                                    return purchaseDetailService.getDetailsFromPurchaseWithProducts(updatedPurchase.getIdPurchase())
                                        .doOnNext(detail -> System.out.println("- Detalle para pago: " + detail.getIdPurchaseDetail()))
                                        .collectList()
                                        .doOnNext(details -> System.out.println("Total detalles para pagos: " + details.size()))
                                        .flatMap(details -> {
                                            System.out.println("Procesando pagos a productores");
                                            return procesarPagosProductores(details, payment);
                                        });
                                });
                        });
                })
                .doOnSuccess(v -> System.out.println("Procesamiento de pago aprobado completado exitosamente"))
                .doOnError(e -> System.err.println("ERROR en procesarPagoAprobado: " + e.getMessage()))
                .doFinally(signal -> System.out.println("=========== FIN procesarPagoAprobado con señal: " + signal + " ==========="))
                .then();
    }
    
    private Mono<Void> procesarPagosProductores(List<PurchaseDetailEntity> details, SimuledPayment payment) {
        System.out.println("=========== INICIO procesarPagosProductores ===========");
        System.out.println("Detalles recibidos: " + details.size() + ", paymentId: " + payment.getId());
    
        // Agrupamos por productor
        Map<UUID, List<PurchaseDetailEntity>> detallesPorProductor = details.stream()
                .collect(Collectors.groupingBy(detail -> {
                    UUID productorId = detail.getProduct() != null ? detail.getProduct().getFkProductor() : null;
                    System.out.println("Agrupando detalle " + detail.getIdPurchaseDetail() +
                            " para producto " + (detail.getProduct() != null ? detail.getProduct().getIdProduct() : "null") +
                            " del productor " + productorId);
                    return productorId;
                }));
    
        System.out.println("Total de productores a pagar: " + detallesPorProductor.size());
    
        // Procesamos cada productor
        return Flux.fromIterable(detallesPorProductor.entrySet())
            .doOnNext(entry -> System.out.println("Procesando productor: " + entry.getKey() +
                    " con " + entry.getValue().size() + " productos"))
            .flatMap((Entry<UUID, List<PurchaseDetailEntity>> entry) -> {
                UUID productorId = entry.getKey();
                List<PurchaseDetailEntity> detallesProductor = entry.getValue();
    
                BigDecimal montoProductor = detallesProductor.stream()
                        .map(detail -> {
                            BigDecimal precio = new BigDecimal(detail.calculatePrice());
                            System.out.println("- Producto: " + detail.getIdPurchaseDetail() +
                                    ", precio: " + precio);
                            return precio;
                        })
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
                System.out.println("Monto total para productor " + productorId + ": " + montoProductor);
    
                return userService.getUserById(productorId)
                    .doOnNext(productor -> System.out.println("Usuario productor encontrado: " +
                            productorId + ", token MP: " +
                            (productor.getMpAccessToken() != null ? "[presente]" : "[ausente]")))
                    .doOnError(e -> System.err.println("ERROR: No se encontró el usuario productor: " +
                            productorId + " - " + e.getMessage()))
                    .flatMap(productor -> {
                        System.out.println("Iniciando transferencia para productor: " + productorId);
                        return transferirFondosAProductor(productorId, productor.getMpAccessToken(), montoProductor, payment.getId())
                            .doOnSuccess(v -> System.out.println("Transferencia completada para productor: " + productorId))
                            .doOnError(e -> System.err.println("ERROR en transferencia para productor " +
                                    productorId + ": " + e.getMessage()));
                    })
                    .then(Mono.empty())
                    .doOnSuccess(v -> System.out.println("Proceso completo para productor: " + productorId));
            })
            .doOnComplete(() -> System.out.println("Todos los pagos a productores procesados"))
            .doOnError(e -> System.err.println("ERROR general en procesarPagosProductores: " + e.getMessage()))
            .doFinally(signal -> System.out.println("=========== FIN procesarPagosProductores con señal: " + signal + " ==========="))
            .then();
    }
    
    private Mono<Object> transferirFondosAProductor(UUID productorId, String mpAccessToken, BigDecimal monto, Long paymentId) {
        System.out.println("=========== INICIO transferirFondosAProductor ===========");
        System.out.println("Parámetros: productorId=" + productorId +
                ", token=" + (mpAccessToken != null ? "[presente]" : "[ausente]") +
                ", monto=" + monto +
                ", paymentId=" + paymentId);
    
        return Mono.defer(() -> {
            try {
                System.out.println("Verificando token de acceso del productor");
                if (mpAccessToken == null || mpAccessToken.isEmpty()) {
                    System.err.println("ERROR: Token de acceso nulo o vacío para el productor " + productorId);
                    return Mono.error(new RuntimeException("Token de acceso inválido para el productor"));
                }
    
                System.out.println("Cambiando al token del productor");
                MercadoPagoConfig.setAccessToken(mpAccessToken);
                System.out.println("Token cambiado correctamente");
    
                System.out.println("Construyendo solicitud de pago");
                JsonObject payoutRequest = new JsonObject();
                payoutRequest.addProperty("amount", monto);
                payoutRequest.addProperty("currency_id", "ARS");
                payoutRequest.addProperty("description", "Pago de venta ID " + paymentId);
                System.out.println("Solicitud construida: " + payoutRequest);
    
                System.out.println("Enviando solicitud a Mercado Pago...");
                HttpResponse<String> response = Unirest.post("https://api.mercadopago.com/v1/payments")
                    .header("Authorization", "Bearer " + mpAccessToken)
                    .header("Content-Type", "application/json")
                    .body(payoutRequest.toString())
                    .asString();
    
                System.out.println("Respuesta recibida: statusCode=" + response.getStatus());
    
                if (response.getStatus() >= 200 && response.getStatus() < 300) {
                    System.out.println("ÉXITO: Transferidos " + monto + " al productor " + productorId);
                    System.out.println("Respuesta: " + response.getBody());
                    return Mono.empty();
                } else {
                    System.err.println("ERROR en la transferencia: statusCode=" + response.getStatus());
                    System.err.println("Cuerpo de respuesta: " + response.getBody());
                    return Mono.error(new RuntimeException("Error en la transferencia: " + response.getBody()));
                }
            } catch (Exception e) {
                System.err.println("EXCEPCIÓN al transferir fondos: " + e.getClass().getName() + ": " + e.getMessage());
                e.printStackTrace();
                return Mono.error(e);
            } finally {
                System.out.println("Restaurando token principal");
                MercadoPagoConfig.setAccessToken("APP_USR-2552125444382264-030609-9af3f586d7ec8eb52060f4db865e5014-447529108");
                System.out.println("Token principal restaurado");
            }
        }).onErrorResume(e -> {
            System.err.println("ERROR MANEJADO: Error procesando pago para productor " + productorId + ": " + e.getMessage());
            e.printStackTrace();
            return Mono.empty();
        })
        .doFinally(signal -> System.out.println("=========== FIN transferirFondosAProductor con señal: " + signal + " ==========="));
    }
    
    
}
