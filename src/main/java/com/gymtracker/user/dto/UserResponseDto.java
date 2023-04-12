package com.gymtracker.user.dto;

import com.gymtracker.user.entity.UserRoles;

public record UserResponseDto(String emailAddress, String login, UserRoles userRole) {
}
