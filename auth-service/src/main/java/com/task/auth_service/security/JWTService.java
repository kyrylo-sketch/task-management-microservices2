package com.task.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        log.info("Generating JWT token for user={}", username);

        Map<String, Object> claims = new HashMap<>();

        log.info("Generating JWT token successfulfor user={}", username);
        //budowanie tokenu
        return Jwts.builder()
                .claims()
                .add(claims)//wlasene dane
                .subject(username)//kto to jest
                .issuedAt(new Date(System.currentTimeMillis()))//kiedy wydano
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 15))//kiedy wygasnie za 15 min
                .and()
                .signWith(getKey())//podpish kluczem HMAC-SHA
                .compact();//zlow w string header.payload.singnature

    }

    //przeksztalcenie string w prawdiwy klusz
    private SecretKey getKey() {
        log.info("Getting JWT secret key for user {}", secretKey);
        //secretKey -> Decoders.BASE64.decode()->byte[]->Keys.hmacShaKeyFor()->SecretKey
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        log.info("Decoding JWT secret key for user={}: successful", secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        //extract the username form jwt token
        return extractClaim(token, Claims::getSubject);
    }

    //generyczna metoda
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //parsowanie i weryfikacja
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())//uzyj tego samego klucza co do generewanie
                .build()
                .parseSignedClaims(token)//weryfikacja podpisu + zdekodowanie
                .getPayload();//zwroc obiek Claims
    }

    //
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
