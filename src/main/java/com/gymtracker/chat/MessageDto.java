package com.gymtracker.chat;

public record MessageDto(String message, Long senderId, Long receiverId, Long ticketId) {
}
