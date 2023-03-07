package com.gymtracker.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public record UserLoginDto(String login, String password) {
};
