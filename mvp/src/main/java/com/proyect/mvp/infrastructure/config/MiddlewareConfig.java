package com.proyect.mvp.infrastructure.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proyect.mvp.infrastructure.config.middlewares.ConfirmPurchaseMiddleware;

@Configuration
public class MiddlewareConfig {
    
    @Bean
    public ConfirmPurchaseMiddleware confirmPurchaseMiddleware() {
        return new ConfirmPurchaseMiddleware();
    }
}
