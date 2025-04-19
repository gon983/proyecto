package com.proyect.mvp.domain.model.entities;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id_message")
    private UUID idMessage;
    
    @Column("user_id")
    @JsonProperty("user_id")
    private UUID userId;
    
    @Column("is_from_company")
    @JsonProperty("is_from_company")
    private boolean isFromCompany;
    
    @Column("content")
    @JsonProperty("content")
    private String content;
    
    @Column("sent_at")
    @JsonProperty("sent_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private LocalDateTime sentAt;
    
    @Column("read")
    @JsonProperty("read")
    private boolean read;
}