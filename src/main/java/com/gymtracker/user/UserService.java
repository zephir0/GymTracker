package com.gymtracker.user;

import com.gymtracker.user.exception.UserNotLoggedInException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


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


}
