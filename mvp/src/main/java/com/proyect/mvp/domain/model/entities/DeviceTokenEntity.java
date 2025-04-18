package com.proyect.mvp.domain.model.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Table("device_tokens") 
@Getter
@NoArgsConstructor
public class DeviceTokenEntity {
    
    @Id
    @Column("id")
    private UUID id;
    
    @Column("user_id")
    private UUID userId;
    
    @Column("device_token")
    private String deviceToken;
    
    @Column("device_type")
    private String deviceType; // "ANDROID", "IOS", "WEB"
    
    @Column("created_at")
    private LocalDateTime createdAt;
}