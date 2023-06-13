package com.gymtracker.chat;

import java.util.List;

public interface MessageService {
    void send(Long ticketId,
              MessageDto messageDto);

    void saveToDatabase(Long ticketId,
                        MessageDto messageDto);

    List<MessageResponseDto> getMessageList(Long ticketId);
}
