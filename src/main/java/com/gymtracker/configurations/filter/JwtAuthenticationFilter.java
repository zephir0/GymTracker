package com.gymtracker.configurations.filter;

import com.gymtracker.auth.token.JwtTokenProvider;
import com.gymtracker.auth.token.JwtTokenStore;
import com.gymtracker.response_model.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.time.LocalDateTime;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenStore jwtTokenStore;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenProvider tokenProvider,
                                   JwtTokenStore jwtTokenStore) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.jwtTokenStore = jwtTokenStore;
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
            if (jwtTokenStore.isTokenValid(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                authenticateUser(request, (UsernamePasswordAuthenticationToken) authentication);
                chain.doFilter(request, response);
            } else {
                throw new SignatureException("Token has expired");
            }

        } catch (SignatureException | ExpiredJwtException ex) {
            handleException(response, ex);
        }
    }

    private void handleException(HttpServletResponse response,
                                 Exception ex) throws IOException {

        String errorMessage;

        if (ex instanceof SignatureException) {
            errorMessage = "Token is invalid";
        } else {
            errorMessage = "Token is expired";
        }

        sendErrorResponse(response, errorMessage);
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
                                   String errorMessage) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, errorMessage, LocalDateTime.now());
        String jsonResponse = "{" +
                "\"httpStatus\":\"" + errorResponse.getHttpStatus() + "\"," +
                "\"messages\":\"" + errorResponse.getMessages() + "\"," +
                "\"timeStamp\":\"" + errorResponse.getTimeStamp() + "\"" +
                "}";

        response.getWriter().write(jsonResponse);
    }
}