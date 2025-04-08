package com.proyect.mvp.infrastructure.config.converters;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Component;

@Component
public class SpatialConverter {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public Point createPoint(Double latitude, Double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude)); // X = lon, Y = lat
    }
}

