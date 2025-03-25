package com.proyect.mvp.application.services;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    
    private static final String SECRET_KEY = "clave-super-segura"; // ¡Cámbiala por una más segura!
    private static final String SALT = "12345678"; // También debería ser seguro y dinámico

    private final TextEncryptor encryptor = Encryptors.text(SECRET_KEY, SALT);

    public String encrypt(String data) {
        return encryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return encryptor.decrypt(encryptedData);
    }
}

