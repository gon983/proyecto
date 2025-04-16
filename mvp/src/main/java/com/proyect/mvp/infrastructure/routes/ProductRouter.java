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
import com.proyect.mvp.application.dtos.update.ProductContaduryUpdateDTO;
import com.proyect.mvp.application.dtos.update.ProductUpdateDTO;
import com.proyect.mvp.application.services.ProductService;

import com.proyect.mvp.domain.model.entities.ProductEntity;
import com.proyect.mvp.infrastructure.security.UserContextService;

import reactor.core.publisher.Mono;

@Configuration 
public class ProductRouter {
    
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductService productService, UserContextService userContext) {
    return route(POST("/api/admin/producto-crear"), request -> createProduct(request, productService))
            .andRoute(PUT("/api/admin/producto-editar"), request -> putProduct(request, productService))
            .andRoute(PUT("/api/admin/producto-contabilidad"), request -> actualizarContaduriaProducto(request, productService))
            .andRoute(GET("/public/productsByCategory/{idCategory}"), request -> getAllProductsFilterByCategory(request, productService))
            .andRoute(GET("/public/products"), request -> getAllProductsFilterByName(request, productService));
}


    private Mono<ServerResponse> createProduct(ServerRequest request, ProductService productService) {
        return request.bodyToMono(ProductCreateDTO.class)
                .flatMap(productService::createProduct)
                .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }
    //

    private Mono<ServerResponse> putProduct(ServerRequest request, ProductService productService){
        return request.bodyToMono(ProductUpdateDTO.class)
                      .flatMap(productService::putProduct)
                      .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }
    private Mono<ServerResponse> actualizarContaduriaProducto(ServerRequest request, ProductService productService){
        return request.bodyToMono(ProductContaduryUpdateDTO.class)
                      .flatMap(productService::actualizarContaduriaProducto)
                      .flatMap(product -> ServerResponse.ok().bodyValue(product));
    }

    

    private Mono<ServerResponse> getAllProductsFilterByName(ServerRequest request, ProductService productService) {
        return request.queryParam("name")
        .map(name -> ServerResponse.ok().body(productService.getAllProductsFilterByName(name), ProductEntity.class))
        .orElseGet(() -> ServerResponse.ok().body(productService.getAllProducts(), ProductEntity.class));
}

    private Mono<ServerResponse> getAllProductsFilterByCategory(ServerRequest request, ProductService productService){
        UUID idCategory = UUID.fromString(request.pathVariable("idCategory"));
        return productService.getAllProductsFilterByCategory(idCategory)
                              .collectList()
                              .flatMap(list -> ServerResponse.ok().bodyValue(list));   
                            
    }
    

                                               

    
}
