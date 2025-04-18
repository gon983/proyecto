package com.proyect.mvp.application.services;




import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FireBaseNotificationService {
    
    private final FirebaseMessaging firebaseMessaging;
    
    public FireBaseNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }
    
    public void sendChatNotification(String token, String messageText) {
        Map<String, String> data = new HashMap<>();
        data.put("type", "chat_message");
        
        Message message = Message.builder()
            .setToken(token)
            .setNotification(Notification.builder()
                .setTitle("Nuevo mensaje")
                .setBody(messageText)
                .build())
            .putAllData(data)
            .build();
        
        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Notificación enviada: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error al enviar notificación: " + e.getMessage());
        }
    }
}