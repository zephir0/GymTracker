package com.gymtracker.user.dto;

import com.gymtracker.user.entity.UserRoles;

import java.time.LocalDateTime;

public record UserRegisterDto(String emailAddress, String login, String password, LocalDateTime creationDate,
                              UserRoles userRoles) {
}
