package com.gymtracker.chat.service;

import com.gymtracker.chat.dto.MessageDto;
import com.gymtracker.chat.dto.MessageResponseDto;

import java.util.List;

public interface MessageService {
    void send(Long ticketId,
              MessageDto messageDto);

    void saveToDatabase(Long ticketId,
                        MessageDto messageDto);

    List<MessageResponseDto> getMessageList(Long ticketId);
}
