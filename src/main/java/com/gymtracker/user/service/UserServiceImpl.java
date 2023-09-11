package com.gymtracker.user.service;

import com.gymtracker.user.UserRepository;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.exception.UserNotLoggedInException;
import com.gymtracker.user.mapper.UserLoginMapper;
import com.gymtracker.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Optional<UserLoginDto> getCredentialsByLogin(String login) {
        return userRepository.findByLogin(login).map(UserLoginMapper::toEntity);
    }


    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(authentication).filter(Authentication::isAuthenticated).map(Authentication::getName).flatMap(userRepository::findByLogin).orElseThrow(() -> new UserNotLoggedInException("User is not logged."));
    }

    @Override
    public UserResponseDto getDetailsOfLoggedUser() {
        User loggedUser = getLoggedUser();
        return userMapper.toUserResponseDto(loggedUser);
    }


    @Override
    public User getReference(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}
