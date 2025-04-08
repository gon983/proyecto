package com.proyect.mvp.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Mono;

@Configuration
public class CorsConfig {
    
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String origin = request.getHeaders().getOrigin();
            
            // Permitimos localhost:3000 como origen
            if ("http://localhost:3000".equals(origin)) {
                response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
                response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Content-Type, Accept, Origin");
                response.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            }
            
            // Respuesta rápida a preflight
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty(); // Better approach for preflight
            }
            
            return chain.filter(exchange);
        };
    }
}


    // @Bean
    // public WebFluxConfigurer corsConfigurer() {
    //     return new WebFluxConfigurer() {
    //         @Override
    //         public void addCorsMappings(@NonNull CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                     .allowedOrigins(ALLOWED_ORIGINS) // Usa los mismos orígenes permitidos
    //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //                     .allowCredentials(true);
    //         }
    //     };
    // }
