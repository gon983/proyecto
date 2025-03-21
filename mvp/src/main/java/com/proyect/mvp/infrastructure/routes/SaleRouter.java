package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

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
        return route(GET("/sales/{idProductor}"), request -> obtenerVentasProductor(request, saleService));
    }
    
    private Mono<ServerResponse> obtenerVentasProductor(ServerRequest request, SaleService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        return saleService.obtenerVentasProductorPorCollectionPoint(idProductor)
                            .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }
    
    // obtener ventas productor las tiene q clasificar por collection point, si fuera posible 
    // recordando en la fecha en la q se tienen q entregar, y ademas debe sumar todas las cantidades de los productos 
    // y todos sus amounts
}
