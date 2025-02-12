package com.proyect.mvp.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;

import reactor.core.publisher.Mono;

@Configuration
public class CorsConfig {

    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request =  exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // Configurar los encabezados CORS
            response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:9090"); // Origen permitido
            response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE"); // Métodos permitidos
            response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*"); // Encabezados permitidos

            // Permitir que el navegador envíe credenciales (si es necesario)
            response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

            // Si es una solicitud OPTIONS (preflight), responder con los encabezados CORS
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"); // Tiempo de caché para preflight
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }

            return chain.filter(exchange);
        };
    }
}
