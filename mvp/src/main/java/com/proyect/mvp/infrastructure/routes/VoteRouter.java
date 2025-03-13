package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;
import com.proyect.mvp.domain.repository.ONGRepository;
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
    public RouterFunction<ServerResponse> voteRoutes(VoteService voteService) {
        return route(POST("/voteProduct/{idUser}"), request -> voteProduct(request, voteService))
        .andRoute(GET("/selectProductByVotationManual/{idCollectionPoint}"), request -> selectProduct(request,voteService));
    }

    private Mono<ServerResponse> voteProduct(ServerRequest request, VoteService voteService) {
        UUID idUser = UUID.fromString(request.pathVariable("idUser"));
        return request.bodyToMono(VoteCreateDTO.class)
                        .flatMap(vote -> voteService.voteProduct(vote, idUser))
                        .flatMap(savedVote -> ServerResponse.ok().bodyValue(savedVote))
                        .onErrorResume(ResponseStatusException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }

    private Mono<ServerResponse> selectProduct(ServerRequest request, VoteService voteService){
        UUID idCollectionPoint =UUID.fromString(request.pathVariable("idCollectionPoint"));
        return voteService.countVotesAndSelectProduct(idCollectionPoint)
                          .then(ServerResponse.ok().build());
    }
    
}
