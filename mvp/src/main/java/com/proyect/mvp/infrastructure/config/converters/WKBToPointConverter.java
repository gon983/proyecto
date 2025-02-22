package com.proyect.mvp.infrastructure.config.converters;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBReader;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class WKBToPointConverter implements Converter<byte[], Point> {
    @Override
    public Point convert(byte[] source) {
        if (source == null) return null;
        try {
            WKBReader reader = new WKBReader();
            return (Point) reader.read(source);
        } catch (Exception e) {
            throw new RuntimeException("Error converting WKB to Point", e);
        }
    }
}
