package com.gymtracker.ticket.dto;

import java.time.LocalDateTime;

public record TicketResponseDto(String subject, String description, LocalDateTime creationDate) {
}
