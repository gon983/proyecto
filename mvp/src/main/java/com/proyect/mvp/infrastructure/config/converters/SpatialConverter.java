package com.proyect.mvp.infrastructure.config.converters;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKBReader;
import org.springframework.stereotype.Component;

@Component
public class SpatialConverter {

    private final WKBWriter wkbWriter = new WKBWriter();
    private final WKBReader wkbReader = new WKBReader();

    public byte[] pointToWKB(Point point) {
        if (point == null) return null;
        return wkbWriter.write(point);
    }

    public Point wkbToPoint(byte[] wkb) {
        if (wkb == null) return null;
        try {
            return (Point) wkbReader.read(wkb);
        } catch (Exception e) {
            throw new RuntimeException("Error converting WKB to Point", e);
        }
    }
}

