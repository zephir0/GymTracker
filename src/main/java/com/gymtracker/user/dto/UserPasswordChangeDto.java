package com.gymtracker.user.dto;

import javax.validation.constraints.NotNull;

public record UserPasswordChangeDto(@NotNull String oldPassword, @NotNull String newPassword) {
}
