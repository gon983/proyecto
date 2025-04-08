package com.proyect.mvp.application.services;




import com.proyect.mvp.application.dtos.create.LocationCreateDTO;
import com.proyect.mvp.application.dtos.response.LocationResponseDTO;
import com.proyect.mvp.application.services.LocationService;
import com.proyect.mvp.domain.model.entities.Location;
import com.proyect.mvp.domain.repository.LocationRepository;
import com.proyect.mvp.infrastructure.config.converters.SpatialConverter;
import lombok.RequiredArgsConstructor;

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
                .map(this::convertToDTO);
    }

    
    public Mono<Void> deleteLocation(UUID locationId) {
        return locationRepository.deleteById(locationId);
    }

    
    public Mono<LocationResponseDTO> createLocation(LocationCreateDTO dto) {
        Location location = new Location();
        location.setUserId(dto.userId());
        location.setAddress(dto.address());
       
        
        // Convertir coordenadas a Point
        location.setCoordinates(spatialConverter.createPoint(
            dto.latitude(), 
            dto.longitude()
        ));

        return locationRepository.save(location)
                .map(this::convertToDTO);
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