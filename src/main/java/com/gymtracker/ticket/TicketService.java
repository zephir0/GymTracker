package com.gymtracker.ticket;

import com.gymtracker.chat.MessageRepository;
import com.gymtracker.ticket.exception.TicketNotFoundException;
import com.gymtracker.ticket.exception.UnauthorizedTicketAccessException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;

    public void createTicket(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.toEntity(ticketDto, userService.getLoggedUser());
        ticketRepository.save(ticket);
    }

    @Transactional
    public void editTicket(Long ticketId,
                           TicketDto ticketDto) {
        ticketRepository.findById(ticketId).map(this::checkAuthorization).ifPresentOrElse(ticket -> {
            ticket.setSubject(ticketDto.subject());
            ticket.setDescription(ticketDto.description());
            ticketRepository.save(ticket);
        }, () -> {
            throw new TicketNotFoundException("Ticket was not found.");
        });
    }

    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.findById(id).map(this::checkAuthorization).ifPresentOrElse(ticket -> {
            messageRepository.deleteAllByTicketId(ticket.getId());
            ticketRepository.deleteById(id);
        }, () -> {
            throw new TicketNotFoundException("Ticket was not found.");
        });
    }

    public Ticket getById(Long id) {
        return ticketRepository.findById(id).map(this::checkAuthorization).orElseThrow(() -> new TicketNotFoundException("Ticket was not found"));
    }

    public List<TicketResponseDto> getAllTicketsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();
        return ticketRepository.findAllByAuthorId(loggedUser.getId()).stream().map(ticketMapper::toDto).collect(Collectors.toList());
    }


    public Ticket checkAuthorization(Ticket ticket) {
        if (ticket.getAuthor().getId().equals(userService.getLoggedUser().getId()) || ticket.getAuthor().getUserRole().equals(UserRoles.ADMIN)) {
            return ticket;
        } else throw new UnauthorizedTicketAccessException("You are not authorized to access this ticket.");

    }

    public Ticket getReference(Long ticketId) {
        return ticketRepository.getReferenceById(ticketId);
    }
}
