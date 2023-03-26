package com.gymtracker.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @MessageMapping("/messages/{ticketId}")
    public void send(@DestinationVariable("ticketId") Long ticketId,
                     @Payload MessageDto messageDto) {
        messageService.send(ticketId, messageDto);
    }

}




