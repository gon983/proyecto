package com.proyect.mvp.infrastructure.routes;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.proyect.mvp.application.dtos.create.ProductCreateDTO;
import com.proyect.mvp.application.dtos.update.ProductUpdateDTO;
import com.proyect.mvp.application.services.ProductService;
import com.proyect.mvp.application.services.StandarProductService;
import com.proyect.mvp.domain.model.entities.ProductEntity;

import reactor.core.publisher.Mono;

@Configuration 
public class ProductRouter {
    
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductService productService) {
        return route(GET("/api/user/{idProducer}"), request -> getProductsByProducer(request, productService))
                .andRoute(POST("/api/productor/products"), request -> createProduct(request, productService))
                .andRoute(PUT("/api/productor/products/{idProduct}"), request -> updateProduct(request, productService))
                .andRoute(GET("/api/user/products"), request -> getAllProducts(request, productService))
                .andRoute(GET("/api/user/optionsForStandarProduct/{idStandarProduct}/{idUser}"), request -> getOptionsForStandarProduct(request, productService));
    }

    private Mono<ServerResponse> getProductsByProducer(ServerRequest request, ProductService productService) {
        UUID idProducer = UUID.fromString(request.pathVariable("idProducer"));
        return ServerResponse.ok().body(productService.getProductsByProducer(idProducer), ProductEntity.class);
    }

    private Mono<ServerResponse> createProduct(ServerRequest request, ProductService productService) {
        return request.bodyToMono(ProductCreateDTO.class)
                .flatMap(productService::createProduct)
                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    private Mono<ServerResponse> updateProduct(ServerRequest request, ProductService productService) {
        return request.bodyToMono(ProductUpdateDTO.class)
                .flatMap(productService::updateProduct)
                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    private Mono<ServerResponse> getAllProducts(ServerRequest request, ProductService productService) {
        return ServerResponse.ok().body(productService.getAllProducts(), ProductEntity.class);
    }

    private Mono<ServerResponse> getOptionsForStandarProduct(ServerRequest request, ProductService productService){
        UUID idStandarProduct = UUID.fromString(request.pathVariable("idStandarProduct"));
        UUID idUser = UUID.fromString(request.pathVariable("idUser"));
        return productService.getOptionsForStandarProduct(idStandarProduct, idUser)
                            .collectList()
                            .flatMap(productsList -> ServerResponse.ok().body(Mono.just(productsList), List.class));                                                

    }
}
