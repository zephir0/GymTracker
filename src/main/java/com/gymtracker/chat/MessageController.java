package com.gymtracker.chat;

import com.gymtracker.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
    public ResponseEntity<SuccessResponse> messageList(@PathVariable Long ticketId) {
        List<MessageResponseDto> messageList = messageService.getMessageList(ticketId);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, messageList, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}




