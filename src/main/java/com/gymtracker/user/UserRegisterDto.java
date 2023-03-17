package com.gymtracker.user;

import java.time.LocalDateTime;

public record UserRegisterDto(String emailAddress, String login, String password, LocalDateTime creationDate,
                              UserRoles userRoles) {
}
