package com.proyect.mvp.infrastructure.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.List;

import com.proyect.mvp.infrastructure.config.converters.PointToWKBConverter;
import com.proyect.mvp.infrastructure.config.converters.WKBToPointConverter;

@Configuration
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory; // Inyecta ConnectionFactory

    public R2dbcConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return this.connectionFactory;
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(new PointToWKBConverter(), new WKBToPointConverter());
    }
}