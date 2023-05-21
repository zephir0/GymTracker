package com.gymtracker.ticket;

import javax.validation.constraints.NotNull;

record TicketDto(@NotNull String subject, @NotNull String description) {
}
