package com.gymtracker.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record UserEmailChangeDto(@Email @NotNull String email) {
}
