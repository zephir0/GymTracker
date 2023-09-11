package com.gymtracker.chat.dto;

import java.time.LocalDateTime;

public record MessageResponseDto(String message, Long senderId, Long receiverId, LocalDateTime creationDate) {
}
