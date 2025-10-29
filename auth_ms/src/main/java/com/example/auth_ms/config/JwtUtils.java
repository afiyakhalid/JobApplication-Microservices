package com.example.auth_ms.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.KeyPair;
import java.util.Date;
@Component
public class JwtUtils {
    private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);

    public String generate(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}