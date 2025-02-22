package com.proyect.mvp.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.n52.jackson.datatype.jts.JtsModule;

@Configuration
public class JacksonConfig {
    
    @Bean
    public JtsModule jtsModule() {
        return new JtsModule();
    }
}
