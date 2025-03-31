package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;
import com.proyect.mvp.domain.repository.ONGRepository;
import com.proyect.mvp.infrastructure.security.UserContextService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.proyect.mvp.application.dtos.create.VoteCreateDTO;
import com.proyect.mvp.application.services.VoteService;

import reactor.core.publisher.Mono;

@Configuration
public class VoteRouter {

    private final VoteService voteService;

    private final ONGRepository ONGRepository;

    VoteRouter(ONGRepository ONGRepository, VoteService voteService) {
        this.ONGRepository = ONGRepository;
        this.voteService = voteService;
    }
    
    @Bean
    public RouterFunction<ServerResponse> voteRoutes(VoteService voteService, UserContextService userContext) {
        return route(POST("/api/user/voteProduct"), request -> voteProduct(request, voteService, userContext))
        .andRoute(GET("/api/admin/selectProductByVotationManual/{idCollectionPoint}"), request -> selectProduct(request,voteService))
        .andRoute(POST("/api/user/calificateProduct/{idUser}"), request -> calificateProduct(request, voteService, userContext));
    }

    private Mono<ServerResponse> voteProduct(ServerRequest request, VoteService voteService, UserContextService userContext) {
        return userContext.getCurrentIdUser()
                          .flatMap(idUser ->
        request.bodyToMono(VoteCreateDTO.class)
                        .flatMap(vote -> voteService.voteProduct(vote, UUID.fromString(idUser)))
                        .flatMap(savedVote -> ServerResponse.ok(200).bodyValue(savedVote))
                        .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()))
                          );
    }

    private Mono<ServerResponse> selectProduct(ServerRequest request, VoteService voteService){
        UUID idCollectionPoint =UUID.fromString(request.pathVariable("idCollectionPoint"));
        return voteService.countVotesAndSelectProduct(idCollectionPoint)
                          .then(ServerResponse.ok(200).build());
    }


    private Mono<ServerResponse> calificateProduct(ServerRequest request, VoteService voteService, UserContextService userContext){
        return userContext.getCurrentIdUser()
                          .flatMap(idUser ->
        request.bodyToMono(VoteCreateDTO.class)
                      .flatMap(vote-> voteService.calificateProduct(vote,UUID.fromString(idUser)))
                      .flatMap(savedVote -> ServerResponse.ok(200).bodyValue(savedVote))
                    .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()))
                          );
    }
    
}
