package com.gymtracker.user.dto;

import javax.validation.constraints.NotNull;

public record UsernameChangeDto(@NotNull String name) {
}
