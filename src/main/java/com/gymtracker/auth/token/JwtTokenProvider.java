package com.gymtracker.auth.token;

import com.gymtracker.configurations.CustomUserDetailsManagerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsManagerConfig userDetailsManagerConfig;
    private final JwtTokenUtils jwtUtils;

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userPrincipal.getUsername());
    }

    public Authentication getAuthentication(String token) {
        if (token != null && validateToken(token)) {
            String userLoginFromJwtToken = jwtUtils.getUsernameFromJwtToken(token);
            UserDetails userDetails = userDetailsManagerConfig.loadUserByUsername(userLoginFromJwtToken);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else return null;
    }

    public boolean validateToken(String token) {
        String userLoginFromJwtToken = jwtUtils.getUsernameFromJwtToken(token);
        UserDetails userDetails = userDetailsManagerConfig.loadUserByUsername(userLoginFromJwtToken);
        return (userLoginFromJwtToken.equals(userDetails.getUsername()) && !jwtUtils.isTokenExpired(token));
    }
}


