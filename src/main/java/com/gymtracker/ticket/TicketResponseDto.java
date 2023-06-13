package com.gymtracker.ticket;

import java.time.LocalDateTime;

public record TicketResponseDto(String subject, String description, LocalDateTime creationDate) {
}
