package com.gymtracker.chat;

import com.gymtracker.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    LocalDateTime localDateTime = LocalDateTime.now();

    @Mapping(target = "message", source = "messageDto.message")
    @Mapping(target = "senderId", source = "user.id")
    @Mapping(target = "receiverId", source = "messageDto.receiverId")
    @Mapping(target = "creationDate", expression = "java(localDateTime.now())")
    Message toEntity(User user,
                     MessageDto messageDto);
}



