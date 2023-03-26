package com.gymtracker.configurations;

import com.gymtracker.chat.ChannelAuthorizationService;
import com.gymtracker.chat.exception.NotAuthorizedToSubscribeChannelException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChannelAuthorizationService authorizationService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(createChannelInterceptor());
    }

    private ChannelInterceptor createChannelInterceptor() {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message,
                                      MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    validateUserAuthorization(accessor);
                }

                return message;
            }
        };
    }

    private void validateUserAuthorization(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        String username = accessor.getUser().getName();
        String channelId = extractChannel(destination);

        if (!authorizationService.isUserAuthorized(channelId, username)) {
            throw new NotAuthorizedToSubscribeChannelException("User " + username + " is not authorized to join channel " + channelId);
        }
    }

    private String extractChannel(String destination) {
        String[] segments = destination.split("/");
        return segments[segments.length - 1];
    }
}