package com.gymtracker.user;

public class UserLoginDtoMapper {
    public static UserLoginDto map(User user) {
        return new UserLoginDto(user.getLogin(), user.getPassword());
    }
}
