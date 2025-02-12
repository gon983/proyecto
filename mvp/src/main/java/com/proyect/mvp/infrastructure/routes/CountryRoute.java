package com.proyect.mvp.infrastructure.routes;

import com.proyect.mvp.domain.model.entities.CountryEntity;
import com.proyect.mvp.application.services.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
@Tag(name = "Countries API", description = "API de gestión de países")
public class CountryRoute {

    @Bean
    @RouterOperations({
        @RouterOperation(path = "/countries", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET,
            operation = @Operation(summary = "Obtener todos los países", description = "Retorna una lista de países",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de países",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryEntity.class)))
                })),
        @RouterOperation(path = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET,
            operation = @Operation(summary = "Obtener país por ID", description = "Retorna un país según su ID",
                parameters = {
                    @Parameter(name = "id", description = "ID del país", required = true)
                },
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "País encontrado",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryEntity.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "País no encontrado")
                })),
        @RouterOperation(path = "/countries", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST,
            operation = @Operation(summary = "Crear un nuevo país", description = "Guarda un nuevo país en la base de datos",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = CountryEntity.class))),
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "País creado",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryEntity.class)))
                })),
        @RouterOperation(path = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT,
            operation = @Operation(summary = "Actualizar un país", description = "Actualiza los datos de un país existente",
                parameters = {
                    @Parameter(name = "id", description = "ID del país a actualizar", required = true)
                },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = CountryEntity.class))),
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "País actualizado",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryEntity.class)))
                })),
        @RouterOperation(path = "/countries/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE,
            operation = @Operation(summary = "Eliminar un país", description = "Borra un país según su ID",
                parameters = {
                    @Parameter(name = "id", description = "ID del país a eliminar", required = true)
                },
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "País eliminado")
                }))
    })
    public RouterFunction<ServerResponse> routes(CountryService countryService) {
        return route(GET("/countries"), request ->
                    ServerResponse.ok().body(countryService.getAllCountries(), CountryEntity.class))
                .andRoute(GET("/countries/{id}"), request -> {
                    String id = request.pathVariable("id");
                    return countryService.getCountryById(id)
                            .flatMap(country -> ServerResponse.ok().bodyValue(country))
                            .onErrorResume(ResponseStatusException.class, e ->
                                    ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
                })
                .andRoute(POST("/countries"), request ->
                        request.bodyToMono(CountryEntity.class)
                                .flatMap(countryService::saveCountry)
                                .flatMap(savedCountry -> ServerResponse.ok().bodyValue(savedCountry))
                                .onErrorResume(ResponseStatusException.class, e ->
                                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage())))
                .andRoute(PUT("/countries/{id}"), request ->
                        request.bodyToMono(CountryEntity.class)
                                .flatMap(updatedCountry -> {
                                    String id = request.pathVariable("id");
                                    return countryService.updateCountry(id, updatedCountry);
                                })
                                .flatMap(updatedCountry -> ServerResponse.ok().bodyValue(updatedCountry))
                                .onErrorResume(ResponseStatusException.class, e ->
                                        ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage())))
                .andRoute(DELETE("/countries/{id}"), request -> {
                    String id = request.pathVariable("id");
                    return countryService.deleteCountryById(id)
                            .then(ServerResponse.noContent().build())
                            .onErrorResume(ResponseStatusException.class, e ->
                                    ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
                });
    }
}