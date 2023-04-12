package com.gymtracker.chat;

import java.time.LocalDateTime;

public record MessageResponseDto(String message, Long senderId, Long receiverId, LocalDateTime creationDate) {
}
