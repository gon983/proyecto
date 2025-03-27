package com.proyect.mvp.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  
    public String generateToken(Authentication authentication) {
        UserAuthenticationDTO principal = (UserAuthenticationDTO) authentication.getPrincipal();
        String username = principal.getUsername();
        String idUser = principal.getIdUser().toString();
        

        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 864_000_000)) // 10 days
            .claim("idUser", idUser)
            .claim("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
            .signWith(secretKey)
            .compact();
        
        log.debug("Token generado: {}", token);
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
            
            log.debug("Username extraído de token: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Error extrayendo username del token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            log.debug("Token validado correctamente");
            return true;
        } catch (Exception e) {
            log.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }
}
