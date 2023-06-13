package com.gymtracker.ticket;

import java.util.List;

public interface TicketService {
    void createTicket(TicketDto ticketDto);

    void editTicket(Long ticketId,
                    TicketDto ticketDto);

    void deleteTicket(Long id);

    Ticket getById(Long id);

    List<TicketResponseDto> getAllTicketsForLoggedUser();

    Ticket checkAuthorization(Ticket ticket);

    Ticket getReference(Long ticketId);
}
