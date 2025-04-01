package com.proyect.mvp.infrastructure.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import com.proyect.mvp.application.dtos.create.PurchaseDetailCreateDTO;
import com.proyect.mvp.application.dtos.requests.ProductsPayedDTO;
import com.proyect.mvp.application.services.PurchaseDetailService;


import reactor.core.publisher.Mono;

@Configuration
public class PurchaseDetailRouter {

    @Bean
    public RouterFunction<ServerResponse> purchaseDetailRoutes(PurchaseDetailService purchaseDetailService){
        return route(POST("/api/user/purchases/details/{purchaseId}/{idCollectionPoint}/{idUser}"), request-> createPurchaseDetail(request, purchaseDetailService))
        .andRoute(GET("/api/user/salesProductor/{idProductor}/{idCollectionPoint}"), request -> obtenerVentasCollectionPointDeProductorSinAbonar(request, purchaseDetailService))
        .andRoute(POST("/api/admin/registerPaymentSales/{idProductor}/{idCollectionPoint}"), request -> registrarPagoVentasCollectionPointDeProductor(request, purchaseDetailService))
        .andRoute(GET("/api/user/neighborhoodPackage/{idCollectionPoint}"), request -> obtenerTodasLasVentasConfirmadasOPagadasDeUnCpSumarizadasPorStandarProduct(request, purchaseDetailService));


    }

    public Mono<ServerResponse> createPurchaseDetail(ServerRequest request, PurchaseDetailService purchaseDetailService){
        UUID fkPurchase = UUID.fromString(request.pathVariable("purchaseId"));
        UUID fkCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        UUID fkBuyer = UUID.fromString(request.pathVariable("idUser"));
        return request.bodyToMono(PurchaseDetailCreateDTO.class)
                        .flatMap(purchaseDetailDto-> purchaseDetailService.createPurchaseDetail(fkBuyer, fkCollectionPoint, fkPurchase, purchaseDetailDto))
                        .flatMap(savedPurchaseDetail -> ServerResponse.ok().bodyValue(savedPurchaseDetail));
    }

   
    
    
    private Mono<ServerResponse> obtenerVentasCollectionPointDeProductorSinAbonar(ServerRequest request, PurchaseDetailService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return saleService.obtenerVentasProductorDeCollectionPoint(idProductor, idCollectionPoint)
                            .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }

    private Mono<ServerResponse> registrarPagoVentasCollectionPointDeProductor(ServerRequest request, PurchaseDetailService saleService ){
        UUID idProductor = UUID.fromString(request.pathVariable("idProductor"));
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return request.bodyToMono(ProductsPayedDTO.class)
                      .flatMapMany(listPayedProduct -> saleService.registrarPagoVentasCollectionPointDeProductor(idProductor, idCollectionPoint, listPayedProduct))
                      .collectList()
                      .flatMap(sales -> ServerResponse.ok().bodyValue(sales));
        
    }


    private Mono<ServerResponse> obtenerTodasLasVentasConfirmadasOPagadasDeUnCpSumarizadasPorStandarProduct(ServerRequest request, PurchaseDetailService purchaseDetailService){
        UUID idCollectionPoint = UUID.fromString(request.pathVariable("idCollectionPoint"));
        return purchaseDetailService.obtenerTodasLasVentasConfirmadasOPagadasDeUnCpSumarizadasPorProduct(idCollectionPoint)
                                    .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    
    }
    
    

