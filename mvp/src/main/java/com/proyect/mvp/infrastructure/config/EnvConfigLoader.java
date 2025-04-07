package com.proyect.mvp.infrastructure.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvConfigLoader {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getNotificationUrl() {
        return dotenv.get("NOTIFICATION_URL");
    }

    public static String getAccessToken() {
        return dotenv.get("ACCESS_TOKEN");
    }

    public static String getSuccessUrl(){
        return dotenv.get("SUCCESS_REDIRECTION_URL");
    }

    public static void main(String[] args) {
        System.out.println("Notification URL: " + getNotificationUrl());
        System.out.println("Access Token: " + getAccessToken());
    }
}