package com.gymtracker.auth.token;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenStore {

    private final ConcurrentHashMap<String, Boolean> tokenStore = new ConcurrentHashMap<>();

    public void storeToken(String token) {
        tokenStore.put(token, Boolean.TRUE);
    }

    public void removeToken(String token) {
        tokenStore.remove(token);
    }

    public boolean isTokenValid(String token) {
        return tokenStore.containsKey(token);
    }

}
