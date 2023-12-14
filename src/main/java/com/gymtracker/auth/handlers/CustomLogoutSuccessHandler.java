package com.gymtracker.auth.handlers;

import com.gymtracker.auth.token.JwtTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final JwtTokenStore jwtTokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            jwtTokenStore.removeToken(token);
        }
        authentication.setAuthenticated(false);
        response.getWriter().write("Logged out successfully");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
