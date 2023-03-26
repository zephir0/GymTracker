package com.gymtracker.auth.token;

import com.gymtracker.configurations.CustomUserDetailsManagerConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final CustomUserDetailsManagerConfig userDetailsManagerConfig;

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

    public String resolveTokenFromHttpRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else return null;
    }

    public Authentication getAuthentication(String token) {
        if (token != null && validateToken(token)) {
            logger.info("jwt during token verification: {}", jwtSecret);

            String userLoginFromJwtToken = getUserLoginFromJwtToken(token);
            UserDetails userDetails = userDetailsManagerConfig.loadUserByUsername(userLoginFromJwtToken);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else return null;
    }

    public boolean validateToken(String token) {
        String userLoginFromJwtToken = getUserLoginFromJwtToken(token);
        UserDetails userDetails = userDetailsManagerConfig.loadUserByUsername(userLoginFromJwtToken);
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
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }


}


