package com.gymtracker.auth.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public String generateToken(String username) {
        Date dateNow = new Date();
        Date expiryDate = new Date(dateNow.getTime() + jwtExpirationInMs);
        return Jwts.builder().setSubject(username).setIssuedAt(dateNow).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.getExpiration();
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expirationDateFromToken = getExpirationDateFromToken(token);
        return expirationDateFromToken.before(new Date());
    }
}