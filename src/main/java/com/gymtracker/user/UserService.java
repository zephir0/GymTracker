package com.gymtracker.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<UserLoginDto> findCredentialsByLogin(String login) {
        return userRepository.findByLogin(login).map(UserLoginDtoMapper::map);
    }
}
