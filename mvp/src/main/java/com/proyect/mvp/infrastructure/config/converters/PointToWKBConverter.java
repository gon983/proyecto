package com.proyect.mvp.infrastructure.config.converters;



import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class PointToWKBConverter implements Converter<Point, byte[]> {

    private final WKBWriter wkbWriter = new WKBWriter();

    @Override
    public byte[] convert(Point source) {
        if (source == null) {
            return null;
        }
        return wkbWriter.write(source);
    }
}
