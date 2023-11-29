package com.gymtracker.ticket;

import com.gymtracker.chat.MessageRepository;
import com.gymtracker.ticket.dto.TicketDto;
import com.gymtracker.ticket.dto.TicketResponseDto;
import com.gymtracker.ticket.exception.TicketNotFoundException;
import com.gymtracker.ticket.exception.UnauthorizedTicketAccessException;
import com.gymtracker.ticket.service.TicketServiceImpl;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    private final Long ticketId = 1L;
    private TicketServiceImpl ticketService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserService userService;
    @Mock
    private TicketMapper ticketMapper;
    private User user;
    private Ticket ticket;
    private TicketDto ticketDto;
    private TicketResponseDto ticketResponseDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketRepository, messageRepository, userService, ticketMapper);

        user = new User();
        user.setId(1L);
        user.setUserRole(UserRoles.USER);
        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setUser(user);
        ticketDto = new TicketDto("subject", "name");
        ticketResponseDto = new TicketResponseDto("subject", "name", LocalDateTime.now());

        when(userService.getLoggedUser()).thenReturn(user);
    }

    @Test
    public void testCreateTicket() {
        when(ticketMapper.toEntity(ticketDto, user)).thenReturn(ticket);
        ticketService.createTicket(ticketDto);

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testEditTicket_TicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.editTicket(ticketId, ticketDto));
    }

    @Test
    public void testEditTicket_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toEntity(ticketDto, user)).thenReturn(ticket);
        ticketService.editTicket(ticketId, ticketDto);

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testDeleteTicket_TicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
    }

    @Test
    public void testDeleteTicket_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        ticketService.deleteTicket(ticketId);

        verify(messageRepository, times(1)).deleteAllByTicketId(ticket.getId());
        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    public void testGetById_TicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.getById(ticketId));
    }

    @Test
    public void testGetById_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        assertEquals(ticket, ticketService.getById(ticketId));
    }

    @Test
    public void testGetAllTicketsForLoggedUser() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        when(ticketRepository.findAllByUserId(user.getId())).thenReturn(tickets);
        when(ticketMapper.toDto(ticket)).thenReturn(ticketResponseDto);

        List<TicketResponseDto> anotherTicketResponseDto = ticketService.getAllTicketsForLoggedUser();
        assertEquals(1, anotherTicketResponseDto.size());
        assertEquals(ticketResponseDto, anotherTicketResponseDto.get(0));
    }

    @Test
    public void testCheckAuthorization_Failure() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        anotherUser.setUserRole(UserRoles.USER);

        ticket.setUser(anotherUser);

        assertThrows(UnauthorizedTicketAccessException.class, () -> ticketService.checkAuthorization(ticket));
    }

    @Test
    public void testCheckAuthorization_Success() {
        assertEquals(ticket, ticketService.checkAuthorization(ticket));
    }

    @Test
    public void testGetReference_Success() {
        when(ticketRepository.getReferenceById(ticketId)).thenReturn(ticket);

        assertEquals(ticket, ticketService.getReference(ticketId));
    }
}
