package com.gymtracker.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record UserRegisterDto(@NotNull @Email String emailAddress, @NotNull String login, @NotNull String password) {
}
