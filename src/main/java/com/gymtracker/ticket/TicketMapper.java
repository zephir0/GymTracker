package com.gymtracker.ticket;

import com.gymtracker.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {


    @Mapping(target = "author", source = "user")
    @Mapping(target = "messageList", ignore = true)
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    Ticket toEntity(TicketDto ticketDto,
                    User user);


    TicketResponseDto toDto(Ticket ticket);
}
