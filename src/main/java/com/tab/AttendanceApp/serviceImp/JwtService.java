package com.tab.AttendanceApp.serviceImp;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.tab.AttendanceApp.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Service

@Slf4j
public class JwtService {

    private final String SECRET_KEY = "57f48f336e083546fd5737e7a4fcf6bb529026d5c0f4fd3e4053285b86529a29";
    private final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(User user) {

        log.info("token:{}kjfvskgjffffffffffffffd@@@@@@@@@@@@@@@");

        return Jwts.builder()
                        .subject(user.getUsername())
                        .claim("role", user.getUserRole().name())
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(getSigninKey(), SignatureAlgorithm.HS256) // 👈 explicitly use HS256
                        .compact();



    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
