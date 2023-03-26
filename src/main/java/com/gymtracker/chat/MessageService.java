package com.gymtracker.chat;

import com.gymtracker.chat.exception.NotAuthorizedToSendMessageInChannelException;
import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.TicketService;
import com.gymtracker.user.User;
import com.gymtracker.user.UserRoles;
import com.gymtracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final TicketService ticketService;
    private final MessageMapper messageMapper;

    public void send(Long ticketId,
                     MessageDto messageDto) {
        User sender = userService.findById(messageDto.senderId());
        Ticket ticket = ticketService.findById(ticketId);

        validateSenderAuthorization(ticket, sender, messageDto.senderId());

        broadcastMessage(ticketId, messageDto);
        saveMessageInDatabase(ticket, sender, messageDto);
    }


    private void validateSenderAuthorization(Ticket ticket,
                                             User sender,
                                             Long senderId) {
        if (!senderId.equals(ticket.getAuthor().getId()) && !sender.getUserRole().equals(UserRoles.ADMIN)) {
            throw new NotAuthorizedToSendMessageInChannelException("You are not authorized to send message in channel id: " + ticket.getId());
        }
    }

    private void broadcastMessage(Long ticketId,
                                  MessageDto messageDto) {
        simpMessagingTemplate.convertAndSend("/topic/messages/" + ticketId, messageDto);
        simpMessagingTemplate.convertAndSend("/topic/messages/admin/" + ticketId, messageDto);
    }

    private void saveMessageInDatabase(Ticket ticket,
                                       User sender,
                                       MessageDto messageDto) {
        Message message = messageMapper.toEntity(sender, messageDto);
        message.setTicket(ticket);
        messageRepository.save(message);
    }
}
