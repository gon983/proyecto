package com.proyect.mvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(scanBasePackages = {"com.proyect.mvp.infrastructure.routes", "com.proyect.mvp.infrastructure.config", "com.proyect.mvp.application.services", "com.proyect.mvp.infrastructure.security"})
public class MvpApplication {

    public static void main(String[] args) {
        // Cargar variables de entorno antes de iniciar Spring
        Dotenv dotenv = Dotenv.configure()
            .directory(".")  // Especifica el directorio donde está el .env
            .ignoreIfMissing()
            .load();

        // Establecer las variables de entorno en el sistema
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        
        SpringApplication.run(MvpApplication.class, args);
    }
}