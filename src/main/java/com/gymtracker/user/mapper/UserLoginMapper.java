package com.gymtracker.user.mapper;

import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.entity.User;

public class UserLoginMapper {
    public static UserLoginDto toEntity(User user) {
        return new UserLoginDto(user.getLogin(), user.getPassword());
    }
}
