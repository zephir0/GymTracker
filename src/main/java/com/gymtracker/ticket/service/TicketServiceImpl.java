package com.gymtracker.ticket.service;

import com.gymtracker.chat.MessageRepository;
import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.TicketMapper;
import com.gymtracker.ticket.TicketRepository;
import com.gymtracker.ticket.dto.TicketDto;
import com.gymtracker.ticket.dto.TicketResponseDto;
import com.gymtracker.ticket.exception.TicketNotFoundException;
import com.gymtracker.ticket.exception.UnauthorizedTicketAccessException;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;

    @Override
    public void createTicket(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.toEntity(ticketDto, userService.getLoggedUser());
        ticketRepository.save(ticket);
    }

    @Transactional
    @Override
    public void editTicket(Long ticketId,
                           TicketDto ticketDto) {
        ticketRepository.findById(ticketId)
                .map(this::checkAuthorization)
                .ifPresentOrElse(ticket -> {
                    ticket.setSubject(ticketDto.subject());
                    ticket.setDescription(ticketDto.description());
                    ticketRepository.save(ticket);
                }, () -> {
                    throw new TicketNotFoundException("Ticket was not found.");
                });
    }

    @Transactional
    @Override
    public void deleteTicket(Long ticketId) {
        ticketRepository.findById(ticketId)
                .map(this::checkAuthorization)
                .ifPresentOrElse(ticket -> {
                    messageRepository.deleteAllByTicketId(ticket.getId());
                    ticketRepository.deleteById(ticketId);
                }, () -> {
                    throw new TicketNotFoundException("Ticket was not found.");
                });
    }

    @Override
    public Ticket getById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(this::checkAuthorization)
                .orElseThrow(
                        () -> new TicketNotFoundException("Ticket was not found")
                );
    }

    @Override
    public List<TicketResponseDto> getAllTicketsForLoggedUser() {
        User loggedUser = userService.getLoggedUser();
        return ticketRepository.findAllByAuthorId(loggedUser.getId())
                .stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public Ticket checkAuthorization(Ticket ticket) {
        if (ticket.getAuthor().getId().equals(userService.getLoggedUser().getId()) || ticket.getAuthor().getUserRole().equals(UserRoles.ADMIN)) {
            return ticket;
        } else throw new UnauthorizedTicketAccessException("You are not authorized to access this ticket.");

    }

    @Override
    public Ticket getReference(Long ticketId) {
        return ticketRepository.getReferenceById(ticketId);
    }
}
