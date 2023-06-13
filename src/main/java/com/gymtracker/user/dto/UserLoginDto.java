package com.gymtracker.user.dto;

import org.springframework.lang.NonNull;

public record UserLoginDto(@NonNull String login, @NonNull String password) {
};
