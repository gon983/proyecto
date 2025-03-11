package com.proyect.mvp.infrastructure.config.middlewares;

import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;


public class ConfirmPurchaseMiddleware {

    private static final String SECRET_KEY = "313206a36978b24f8048cf0b28b825d2aef2dd2e37b672520158614ce0d5dd96";

    
    public Mono<Boolean> validate(ServerRequest request) {
        return Mono.justOrEmpty(request.headers().firstHeader("x-signature"))
                .flatMap(signatureHeader -> {
                    String requestIdHeader = request.headers().firstHeader("x-request-id");
                    if (requestIdHeader == null) return Mono.just(false);

                    // Extraer ts y v1
                    String[] parts = signatureHeader.split(",");
                    String ts = null, v1 = null;
                    for (String part : parts) {
                        String[] keyValue = part.split("=");
                        if (keyValue.length == 2) {
                            if ("ts".equals(keyValue[0])) ts = keyValue[1];
                            else if ("v1".equals(keyValue[0])) v1 = keyValue[1];
                        }
                    }
                    if (ts == null || v1 == null) return Mono.just(false);

                    // Crear la cadena de validación
                    String signedTemplate = String.format("id:[data.id_url];request-id:%s;ts:%s;", requestIdHeader, ts);
                    String expectedSignature = new HmacUtils("HmacSHA256", SECRET_KEY)
                            .hmacHex(signedTemplate.getBytes(StandardCharsets.UTF_8));

                    return Mono.just(expectedSignature.equals(v1));
                });
    }
}

