package com.proyect.mvp.infrastructure.routes;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.proyect.mvp.application.services.CategoryService;
import com.proyect.mvp.domain.model.entities.CategoryEntity;
import com.proyect.mvp.dtos.create.CategoryCreateDTO;

import reactor.core.publisher.Mono;

@Configuration 
public class CategoryRouter {
    
    @Bean
    public RouterFunction<ServerResponse> categoryRoutes( CategoryService categoryService ) {
        return route(GET("/categories"), request -> getAllCategories(request, categoryService))
                .andRoute(GET("/categories/{id}"), request -> getCategoryById(request, categoryService))
                .andRoute(POST("/categories"), request -> createCategory(request, categoryService));
    }

    private Mono<ServerResponse> createCategory(ServerRequest request, CategoryService categoryService) {
        return request.bodyToMono(CategoryCreateDTO.class)
                .flatMap(category -> categoryService.saveNewCategory(category))
                .flatMap(savedCategory -> ServerResponse.ok().bodyValue(savedCategory));
    }

    private Mono<ServerResponse> getCategoryById(ServerRequest request, CategoryService categoryService) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return categoryService.getCategoryById(id)
                .flatMap(category -> ServerResponse.ok().bodyValue(category));
    }

    private Mono<ServerResponse> getAllCategories(ServerRequest request, CategoryService categoryService) {
        return ServerResponse.ok().body(categoryService.getAllCategories(), CategoryEntity.class);
    }
}
