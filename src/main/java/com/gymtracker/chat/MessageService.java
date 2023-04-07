package com.gymtracker.chat;

import com.gymtracker.chat.exception.NotAuthorizedToGetMessagesException;
import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.TicketService;
import com.gymtracker.ticket.exception.TicketNotFoundException;
import com.gymtracker.user.UserService;
import com.gymtracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final TicketService ticketService;


    public void send(Long ticketId,
                     MessageDto messageDto) {
        broadcastMessage(ticketId, messageDto);
        saveToDatabase(ticketId, messageDto);
    }


    public void saveToDatabase(Long ticketId,
                               MessageDto messageDto) {
        User sender = userService.getReference(messageDto.senderId());
        Ticket ticket = ticketService.getReference(ticketId);
        Message message = messageMapper.toEntity(sender, messageDto, ticket);
        messageRepository.save(message);
    }


    public List<Message> getMessageList(Long ticketId) {
        User loggedUser = userService.getLoggedUser();

        return ticketService.findById(ticketId).map(ticket -> {
            if (ticket.getAuthor().getId().equals(loggedUser.getId())) {
                return ticket.getMessageList();
            } else
                throw new NotAuthorizedToGetMessagesException("You are not authorized to get messages in that ticket channel");
        }).orElseThrow(() -> new TicketNotFoundException("Ticket doesn't exist"));
    }


    private void broadcastMessage(Long ticketId,
                                  MessageDto messageDto) {
        simpMessagingTemplate.convertAndSend("/topic/messages/" + ticketId, messageDto);
        simpMessagingTemplate.convertAndSend("/topic/messages/admin/" + ticketId, messageDto);
    }


}