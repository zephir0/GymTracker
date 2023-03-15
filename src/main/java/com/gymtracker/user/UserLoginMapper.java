package com.gymtracker.user;

public class UserLoginMapper {
    public static UserLoginDto toEntity(User user) {
        return new UserLoginDto(user.getLogin(), user.getPassword());
    }
}
