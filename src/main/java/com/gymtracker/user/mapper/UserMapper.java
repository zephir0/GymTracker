package com.gymtracker.user.mapper;

import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userRole", target = "userRole")
    UserResponseDto toUserResponseDto(User user);
}
