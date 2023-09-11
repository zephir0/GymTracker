package com.gymtracker.configurations.interceptor;

import com.gymtracker.chat.service.ChannelAuthorizationService;
import com.gymtracker.chat.exception.ChannelSubscriptionException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final ChannelAuthorizationService authorizationService;


    @Override
    public Message<?> preSend(Message<?> message,
                              MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            validateUserAuthorization(accessor);
        }

        return message;
    }

    private void validateUserAuthorization(SimpMessageHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        String username = accessor.getUser().getName();
        String channelId = extractChannel(destination);

        if (!authorizationService.isUserAuthorized(channelId, username)) {
            throw new ChannelSubscriptionException("User " + username + " is not authorized to join channel " + channelId);
        }
    }

    private String extractChannel(String destination) {
        String[] segments = destination.split("/");
        return segments[segments.length - 1];
    }
}

