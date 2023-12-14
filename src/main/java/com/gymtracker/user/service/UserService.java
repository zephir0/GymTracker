package com.gymtracker.user.service;

import com.gymtracker.user.dto.*;
import com.gymtracker.user.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<UserLoginDto> getCredentialsByLogin(String login);

    User getLoggedUser();

    void changePassword(UserPasswordChangeDto userPasswordChangeDto);

    void changeEmail(UserEmailChangeDto userEmailChangeDto);

    void changeUsername(UsernameChangeDto usernameChangeDto);

    void deleteAccount();

    UserResponseDto getDetailsOfLoggedUser();

    User getReference(Long userId);
}
