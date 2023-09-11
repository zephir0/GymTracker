package com.gymtracker.ticket.dto;

import javax.validation.constraints.NotNull;

public record TicketDto(@NotNull String subject, @NotNull String description) {
}
