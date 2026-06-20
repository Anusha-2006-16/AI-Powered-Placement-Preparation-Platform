package com.placement_prep_platform.placement_prep_platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET=
            "placementprepplatformjwtsecretkey2026secure123";



    private static Key getSignKey(){
        return
                Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public  String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()+1000 *60*60
                        )
                )
                .signWith(
                        getSignKey()
                ).compact();
    }
    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(
                        (SecretKey)getSignKey()
                ).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    private static Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public   String extractUsername(String token){

        return extractAllClaims(token)
                .getSubject();
    }

    public boolean validateToken(
            String token,
            String email){

        String extractedEmail =
                extractUsername(token);

        return extractedEmail.equals(email)
                && !isTokenExpired(token);
    }
    private boolean isTokenExpired(
            String token){

        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
