package com.gymtracker.user.service;

import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<UserLoginDto> getCredentialsByLogin(String login);

    User getLoggedUser();

    void deleteAccount();

    UserResponseDto getDetailsOfLoggedUser();

    User getReference(Long userId);
}
