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
@Table("chat_messages") 
@Getter
@NoArgsConstructor
public class ChatMessageEntity {
    
    @Id
    @Column("id_message")
    private UUID idMessage;
    
    @Column("user_id")
    private UUID userId;
    
    @Column("is_from_company")
    private boolean isFromCompany;
    
    @Column("content")
    private String content;
    
    @Column("sent_at")
    private LocalDateTime sentAt;
    
    @Column("read")
    private boolean read;
}