package com.gymtracker.ticket.service;

import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.dto.TicketDto;
import com.gymtracker.ticket.dto.TicketResponseDto;

import java.util.List;

public interface TicketService {
    void createTicket(TicketDto ticketDto);

    void editTicket(Long ticketId,
                    TicketDto ticketDto);

    void deleteTicket(Long ticketId);

    Ticket getById(Long ticketId);

    List<TicketResponseDto> getAllTicketsForLoggedUser();

    Ticket checkAuthorization(Ticket ticket);

    Ticket getReference(Long ticketId);
}
