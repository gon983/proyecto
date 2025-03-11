package com.proyect.mvp.infrastructure.config;



import com.proyect.mvp.infrastructure.middlewares.ConfirmPurchaseMiddleware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiddlewareConfig {
    
    @Bean
    public ConfirmPurchaseMiddleware confirmPurchaseMiddleware() {
        return new ConfirmPurchaseMiddleware();
    }
}
