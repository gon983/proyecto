package com.proyect.mvp.infrastructure.config.middlewares;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;


public class ConfirmPurchaseMiddleware {

    private static final String SECRET_KEY = "313206a36978b24f8048cf0b28b825d2aef2dd2e37b672520158614ce0d5dd96";
    
    public Mono<Boolean> validate(ServerRequest request) {
        return request.bodyToMono(String.class)
                .flatMap(body -> {
                    // Obtenemos los headers necesarios
                    String signatureHeader = request.headers().firstHeader("x-signature");
                    String requestIdHeader = request.headers().firstHeader("x-request-id");
                    
                    // Si no hay signature header, falla la validación
                    if (signatureHeader == null) {
                        return Mono.just(false);
                    }
                    
                    // Extraer ts y v1 del header x-signature
                    String ts = null, v1 = null;
                    String[] parts = signatureHeader.split(",");
                    for (String part : parts) {
                        String[] keyValue = part.trim().split("=");
                        if (keyValue.length == 2) {
                            if ("ts".equals(keyValue[0])) ts = keyValue[1];
                            else if ("v1".equals(keyValue[0])) v1 = keyValue[1];
                        }
                    }
                    
                    if (ts == null || v1 == null) {
                        return Mono.just(false);
                    }
                    
                    // Extraer data.id del body (si fuera necesario)
                    // Para el ejemplo sencillo, usamos el Data ID de la URL o un valor fijo
                    // En producción deberías parsear el JSON para obtener data.id
                    
                    // Construir el signedTemplate según la documentación
                    StringBuilder templateBuilder = new StringBuilder();
                    
                    // Agregar data.id si viene como parámetro en la URL
                    String dataId = request.queryParam("data.id").orElse(null);
                    if (dataId != null) {
                        templateBuilder.append("id:").append(dataId.toLowerCase()).append(";");
                    }
                    
                    // Agregar request-id si existe en los headers
                    if (requestIdHeader != null) {
                        templateBuilder.append("request-id:").append(requestIdHeader).append(";");
                    }
                    
                    // Agregar timestamp
                    templateBuilder.append("ts:").append(ts).append(";");
                    
                    String signedTemplate = templateBuilder.toString();
                    System.out.println("Template generado: " + signedTemplate);
                    
                    // Generar firma HMAC
                    String expectedSignature = new HmacUtils("HmacSHA256", SECRET_KEY)
                            .hmacHex(signedTemplate.getBytes(StandardCharsets.UTF_8));
                    
                    System.out.println("Firma esperada: " + expectedSignature);
                    System.out.println("Firma recibida: " + v1);
                    
                    return Mono.just(expectedSignature.equals(v1));
                });
    }
}

