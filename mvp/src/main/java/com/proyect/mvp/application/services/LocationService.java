package com.proyect.mvp.application.services;



import org.locationtech.jts.geom.Point;
import com.proyect.mvp.application.dtos.create.LocationCreateDTO;
import com.proyect.mvp.application.dtos.response.LocationResponseDTO;
import com.proyect.mvp.application.services.LocationService;
import com.proyect.mvp.domain.model.entities.Location;
import com.proyect.mvp.domain.repository.LocationRepository;
import com.proyect.mvp.infrastructure.config.converters.SpatialConverter;
import com.proyect.mvp.infrastructure.exception.CoverageAreaException;
import com.proyect.mvp.infrastructure.security.UserContextService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final SpatialConverter spatialConverter;
    

    public LocationService(LocationRepository locationRepository, SpatialConverter spatialConverter){
        this.locationRepository = locationRepository;
        this.spatialConverter = spatialConverter;
    }

    
    public Flux<LocationResponseDTO> getLocationsByUser(UUID userId) {
        return locationRepository.findByUserId(userId)
                .filter(location -> location.isActive())
                .map(this::convertToDTO);
    }

    public Mono<LocationResponseDTO> getLocationById(UUID idLocation){
        return locationRepository.findById(idLocation)
                                .map(this::convertToDTO);

    }

    public Mono<Location> getLocationEntityById(UUID idLocation){
        return locationRepository.findById(idLocation);

    }

    
    public Mono<Void> deleteLocation(UUID locationId) {
        return locationRepository.deleteLogical(locationId);
    }

    
    public Mono<LocationResponseDTO> createLocation(LocationCreateDTO dto) {
    // Validar cobertura antes de crear la ubicación
    if (!isWithinCoverage(dto.latitude(), dto.longitude())) {
        throw new CoverageAreaException("La ubicación está fuera de la zona de cobertura");
    }

    Location location = new Location();
    location.setUserId(dto.userId());
    location.setAddress(dto.address());
    
    location.setCoordinates(spatialConverter.createPoint(
        dto.latitude(), 
        dto.longitude()
    ));

    return locationRepository.save(location)
            .map(this::convertToDTO);
}

private boolean isWithinCoverage(double latitude, double longitude) {
    // Aquí defines tus polígonos de cobertura. Ejemplo:
    List<List<Point>> coverageAreas = new ArrayList<>();
    
    // Cada área de cobertura es un polígono representado por una lista de puntos
    // (Ejemplo: un rectángulo definido por 4 puntos)
    // List<Point> cordoba = Arrays.asList(
    //     spatialConverter.createPoint(-31.352665656299497, -64.24484842819409), // Esquina noroeste
    //     spatialConverter.createPoint(-31.367690330374526, -64.12850971060341), // Esquina noreste
    //     spatialConverter.createPoint(-31.459443975896832, -64.13086187027362), // Esquina sureste
    //     spatialConverter.createPoint(-31.455198525939064, -64.26497652827287)  // Esquina suroeste
    // );
    
    // coverageAreas.add(cordoba);

    List<Point> cordoba = Arrays.asList(
        spatialConverter.createPoint(-31.344081931960893, -64.28770017098205), // Esquina noroeste
        spatialConverter.createPoint(-31.34773825848305, -64.10146792407215), // Esquina noreste
        spatialConverter.createPoint(-31.50943148591176, -64.09492443988795), // Esquina sureste
        spatialConverter.createPoint(-31.51615860760856, -64.29298440062402)  // Esquina suroeste
    );
    
    coverageAreas.add(cordoba);
    
    // Agrega más áreas si es necesario
    
    // Verificar si el punto está dentro de alguna de las áreas de cobertura
    Point pointToCheck = spatialConverter.createPoint(latitude, longitude);
    return coverageAreas.stream()
            .anyMatch(polygon -> isPointInPolygon(pointToCheck, polygon));
}

    // Algoritmo Ray Casting para determinar si un punto está dentro de un polígono
    private boolean isPointInPolygon(Point point, List<Point> polygon) {
        int intersectCount = 0;
        double precision = 1e-9;
        
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());
            
            if (point.getY() > Math.min(p1.getY(), p2.getY()) && 
                point.getY() <= Math.max(p1.getY(), p2.getY()) && 
                point.getX() <= Math.max(p1.getX(), p2.getX())) {
                
                double xIntersection = (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) 
                                        / (p2.getY() - p1.getY()) + p1.getX();
                
                if (Math.abs(p2.getY() - p1.getY()) < precision || point.getX() <= xIntersection) {
                    intersectCount++;
                }
            }
        }
        return (intersectCount % 2) == 1;
    }



    private LocationResponseDTO convertToDTO(Location location) {
        return new LocationResponseDTO(
            location.getId().toString(),                      // UUID → String
            location.getAddress(),
            location.getCoordinates().getY(),                 // Latitude
            location.getCoordinates().getX()               // Longitude
          
        );
    }
    
}