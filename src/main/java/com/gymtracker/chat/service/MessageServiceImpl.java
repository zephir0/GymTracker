package com.gymtracker.chat.service;

import com.gymtracker.chat.Message;
import com.gymtracker.chat.MessageMapper;
import com.gymtracker.chat.MessageRepository;
import com.gymtracker.chat.dto.MessageDto;
import com.gymtracker.chat.dto.MessageResponseDto;
import com.gymtracker.ticket.Ticket;
import com.gymtracker.ticket.service.TicketService;
import com.gymtracker.user.service.UserService;
import com.gymtracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final TicketService ticketService;


    @Override
    public void send(Long ticketId,
                     MessageDto messageDto) {
        broadcastMessage(ticketId, messageDto);
        saveToDatabase(ticketId, messageDto);
    }


    @Override
    public void saveToDatabase(Long ticketId,
                               MessageDto messageDto) {
        User sender = userService.getReference(messageDto.senderId());
        Ticket ticket = ticketService.getReference(ticketId);
        Message message = messageMapper.toEntity(sender, messageDto, ticket);
        messageRepository.save(message);
    }


    @Override
    public List<MessageResponseDto> getMessageList(Long ticketId) {
        Ticket ticket = ticketService.getById(ticketId);
        return convertMessagesToDtoList(ticket.getMessageList());
    }

    private List<MessageResponseDto> convertMessagesToDtoList(List<Message> messages) {
        return messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }


    private void broadcastMessage(Long ticketId,
                                  MessageDto messageDto) {
        simpMessagingTemplate.convertAndSend("/topic/messages/" + ticketId, messageDto);
        simpMessagingTemplate.convertAndSend("/topic/messages/admin/" + ticketId, messageDto);
    }


}
