package com.proyect.mvp.infrastructure.config;

// O el paquete de tu configuración

import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JtsConfig { // Puedes usar un nombre más descriptivo si lo deseas

    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 4326); // SRID 4326 (WGS84)
    }
}
