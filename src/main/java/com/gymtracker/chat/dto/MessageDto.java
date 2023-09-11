package com.gymtracker.chat.dto;

import javax.validation.constraints.NotNull;

public record MessageDto(@NotNull String message, @NotNull Long senderId, @NotNull Long receiverId, Long ticketId) {
}
