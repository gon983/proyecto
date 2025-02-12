package com.proyect.mvp.infrastructure.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MVP API") // Título de tu API
                        .description("Proyecto MVP con Spring WebFlux") // Descripción de tu API
                        .version("1.0") // O la versión de tu API
                        .termsOfService("http://example.com/terms")
                        .contact(new io.swagger.v3.oas.models.info.Contact().name("Gonzalo Dutari").email("gonzalo@example.com"))
                        .license(new io.swagger.v3.oas.models.info.License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
