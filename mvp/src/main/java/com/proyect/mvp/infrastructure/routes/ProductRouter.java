package com.proyect.mvp.infrastructure.routes;
import com.proyect.mvp.application.services.ProductService;
import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.dtos.create.ProductCreateDTO;
import com.proyect.mvp.dtos.update.ProductUpdateDTO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import java.util.UUID;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductService productService) {
        return org.springframework.web.reactive.function.server.RouterFunctions.route()
                .GET("/products/producer/{idProducer}",
                        req -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(productService.getProductsByProducer(UUID.fromString(req.pathVariable("idProducer"))), ProductEntity.class))
                .POST("/products",
                        req -> req.bodyToMono(ProductCreateDTO.class)
                                .flatMap(productService::createProduct)
                                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product)))
                .PUT("/products/{idProduct}",
                        req -> req.bodyToMono(ProductUpdateDTO.class)
                                .flatMap(productService::updateProduct)
                                .flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(product)))
                .GET("/products", 
                        req -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                                .body(productService.getAllProducts(),ProductEntity.class))                
                .build();
    }
}