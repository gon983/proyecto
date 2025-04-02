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
import com.proyect.mvp.infrastructure.security.UserContextService;

import reactor.core.publisher.Mono;

@Configuration 
public class ProductRouter {
    
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductService productService, UserContextService userContext) {
        return route(GET("/api/user/products/productor"), request -> getProductsByProducer(request, productService, userContext))
                .andRoute(POST("/api/admin/products"), request -> createProduct(request, productService))
                .andRoute(PUT("/api/productor/products/{idProduct}"), request -> updateProduct(request, productService))
                .andRoute(GET("/api/public/products"), request -> getAllProductsFilterByName(request, productService))
                .andRoute(GET("/api/user/optionsForStandarProduct/{idStandarProduct}"), request -> getOptionsForStandarProduct(request, productService, userContext));
    }

    private Mono<ServerResponse> getProductsByProducer(ServerRequest request, ProductService productService, UserContextService userContext) {
        return userContext.getCurrentIdUser()
                    .flatMap(idProducer -> ServerResponse.ok().body(productService.getProductsByProducer(UUID.fromString(idProducer)), ProductEntity.class));
         
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

    private Mono<ServerResponse> getAllProductsFilterByName(ServerRequest request, ProductService productService) {
        return request.queryParam("name")
        .map(name -> ServerResponse.ok().body(productService.getAllProductsFilterByName(name), ProductEntity.class))
        .orElseGet(() -> ServerResponse.ok().body(productService.getAllProducts(), ProductEntity.class));
}
    

    private Mono<ServerResponse> getOptionsForStandarProduct(ServerRequest request, ProductService productService, UserContextService userContext){
        UUID idStandarProduct = UUID.fromString(request.pathVariable("idStandarProduct"));
        return userContext.getCurrentIdUser()
                          .flatMap(idUser ->
        productService.getOptionsForStandarProduct(idStandarProduct, UUID.fromString(idUser))
                            .collectList()
                            .flatMap(productsList -> ServerResponse.ok().body(Mono.just(productsList), List.class))
    );                                               

    }
}
