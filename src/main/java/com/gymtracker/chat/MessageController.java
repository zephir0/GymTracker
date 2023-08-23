package com.gymtracker.chat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets/{ticketId}/messages")
@Api(tags = "Chat Messages")
public class MessageController {
    private final MessageService messageService;

    @ApiOperation("Send a chat message")
    @MessageMapping("/messages/{ticketId}")
    public void send(@DestinationVariable("ticketId") Long ticketId,
                     @Payload MessageDto messageDto) {
        messageService.send(ticketId, messageDto);
    }

    @ApiOperation("Get list of chat messages")
    @GetMapping()
    public ResponseEntity<List<MessageResponseDto>> getMessageList(@PathVariable Long ticketId) {
        List<MessageResponseDto> messageList = messageService.getMessageList(ticketId);
        return new ResponseEntity<>(messageList, HttpStatus.OK);
    }
}
