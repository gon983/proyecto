package com.proyect.mvp.infrastructure.routes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class Sample {
    @Bean
    public RouterFunction<ServerResponse> simpleRoute() {
        return route(GET("/simple"), request -> ServerResponse.ok().bodyValue("Hello, Swagger!"));
    }
}