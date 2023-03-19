package com.gymtracker.auth.token;

import com.gymtracker.configurations.CustomUserDetailsManagerConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final CustomUserDetailsManagerConfig userDetailsManagerConfig;
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   CustomUserDetailsManagerConfig userDetailsManagerConfig,
                                   JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.userDetailsManagerConfig = userDetailsManagerConfig;
        this.tokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = extractTokenFromHeader(header);
            String userLoginFromJwtToken = tokenProvider.getUserLoginFromJwtToken(token);
            UserDetails userDetails = userDetailsManagerConfig.loadUserByUsername(userLoginFromJwtToken);

            if (tokenProvider.validateToken(token, userDetails)) {
                authenticateUser(request, userDetails);
                chain.doFilter(request, response);
            } else {
                chain.doFilter(request, response);
            }
        } catch (SignatureException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token is invalid");
        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token is expired");
        }
    }

    private String extractTokenFromHeader(String header) {
        return header.substring(7);
    }

    private void authenticateUser(HttpServletRequest request,
                                  UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   int statusCode,
                                   String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String message = "{\"error\": \"" + errorMessage + "\"}";

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(message);
            printWriter.flush();
        }
    }

}

