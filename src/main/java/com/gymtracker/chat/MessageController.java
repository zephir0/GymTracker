package com.gymtracker.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets/{ticketId}/messages")
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/messages/{ticketId}")
    public void send(@DestinationVariable("ticketId") Long ticketId,
                     @Payload MessageDto messageDto) {
        messageService.send(ticketId, messageDto);
    }

    @GetMapping()
    public List<Message> messageList(@PathVariable Long ticketId) {
        return messageService.getMessageList(ticketId);
    }

}



