package com.gymtracker.user;

import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.exception.UserNotLoggedInException;
import com.gymtracker.user.mapper.UserLoginMapper;
import com.gymtracker.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public Optional<UserLoginDto> findCredentialsByLogin(String login) {
        return userRepository.findByLogin(login).map(UserLoginMapper::toEntity);
    }


    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.of(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .flatMap(userRepository::findByLogin)
                .orElseThrow(() -> new UserNotLoggedInException("User is not logged."));
    }

    public UserResponseDto getLoggedUserInfo() {
        User loggedUser = getLoggedUser();
        return userMapper.toUserResponseDto(loggedUser);
    }


    public User getReference(Long userId) {
        return userRepository.getReferenceById(userId);
    }


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }


}
