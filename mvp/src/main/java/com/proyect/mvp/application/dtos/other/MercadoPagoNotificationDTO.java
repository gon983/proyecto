package com.proyect.mvp.application.dtos.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MercadoPagoNotificationDTO {
    private String action;
    
    @JsonProperty("api_version")
    private String apiVersion;
    
    private NotificationData data;
    
    @JsonProperty("date_created")
    private String dateCreated;
    
    private String id;
    
    @JsonProperty("live_mode")
    private boolean liveMode;
    
    private String type;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class NotificationData {
        private String id;
    }
}

