package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.proyect.mvp.application.dtos.requests.ProductsPayedDTO;
import com.proyect.mvp.application.dtos.response.CollectionPointSalesDTO;
import com.proyect.mvp.application.services.SaleService;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.List;
import java.util.UUID;

import reactor.core.publisher.Mono;

@Configuration
public class SaleRouter {

    @Bean
    RouterFunction<ServerResponse> saleRoutes(SaleService saleService){
        return route(GET("/salesProductor/{idProductor}"), request -> obtenerVentasProductorSinAbonar(request, saleService))
            .andRoute(GET("/salesProductor/{idProductor}/{idCollectionPoint}"), request -> obtenerVentasCollectionPointDeProductorSinAbonar(request, saleService))
            .andRoute(POST("registerPaymentSales/{idProductor}/{idCollectionPoint}"), request -> registrarPagoVentasCollectionPointDeProductor(request, saleService));
    }
    
    private Mono<ServerResponse> obtenerVentasProductorSinAbonar(ServerRequest request, SaleService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        return saleService.obtenerVentasProductorPorCollectionPoint(idProductor)
                            .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }
    
    private Mono<ServerResponse> obtenerVentasCollectionPointDeProductorSinAbonar(ServerRequest request, SaleService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return saleService.obtenerVentasProductorDeCollectionPoint(idProductor, idCollectionPoint)
                            .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }

    private Mono<ServerResponse> registrarPagoVentasCollectionPointDeProductor(ServerRequest request, SaleService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return request.bodyToMono(ProductsPayedDTO.class)
                      .flatMapMany(listPayedProduct -> saleService.registrarPagoVentasCollectionPointDeProductor(idProductor, idCollectionPoint, listPayedProduct))
                      .collectList()
                      .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }
}
