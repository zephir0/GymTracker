package com.gymtracker.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        logger.info("jwtSecret during token generation: {}", jwtSecret);

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date dateNow = new Date();
        Date expiryDate = new Date(dateNow.getTime() + jwtExpirationInMs);
        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(dateNow).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateToken(String token,
                                 UserDetails userDetails) {
        String userLoginFromJwtToken = getUserLoginFromJwtToken(token);
        return (userLoginFromJwtToken.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUserLoginFromJwtToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.getSubject();
    }


    private boolean isTokenExpired(String token) {
        Date expirationDateFromToken = getExpirationDateFromToken(token);
        return expirationDateFromToken.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        Claims claimsFromToken = getClaimsFromToken(token);
        return claimsFromToken.getExpiration();
    }

    private Claims getClaimsFromToken(String token) {
        logger.info("jwtSecret during token verification: {}", jwtSecret);

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}


