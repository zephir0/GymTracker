package com.gymtracker.configurations.filter;

import com.gymtracker.auth.token.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
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
            Authentication authentication = tokenProvider.getAuthentication(token);
            authenticateUser(request, (UsernamePasswordAuthenticationToken) authentication);
            chain.doFilter(request, response);
        } catch (SignatureException | ExpiredJwtException ex) {
            handleException(response, ex);
        }
    }

    private void handleException(HttpServletResponse response,
                                 Exception ex) throws IOException {
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        String errorMessage;

        if (ex instanceof SignatureException) {
            errorMessage = "Token is invalid";
        } else {
            errorMessage = "Token is expired";
        }

        sendErrorResponse(response, statusCode, errorMessage);
    }

    private String extractTokenFromHeader(String header) {
        return header.substring(7);
    }

    private void authenticateUser(HttpServletRequest request,
                                  UsernamePasswordAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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