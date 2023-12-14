package com.gymtracker.user.service;

import com.gymtracker.user.UserRepository;
import com.gymtracker.user.dto.*;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.exception.EmailAddressTakenException;
import com.gymtracker.user.exception.IncorrectOldPasswordException;
import com.gymtracker.user.exception.UserNotLoggedInException;
import com.gymtracker.user.exception.UsernameTakenException;
import com.gymtracker.user.mapper.UserLoginMapper;
import com.gymtracker.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public void changePassword(UserPasswordChangeDto userPasswordChangeDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User loggedUser = getLoggedUser();

        System.out.println(userPasswordChangeDto.oldPassword());
        if (passwordEncoder.matches(userPasswordChangeDto.oldPassword(), loggedUser.getPassword())) {
            String newPass = userPasswordChangeDto.newPassword();
            loggedUser.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(loggedUser);
        } else {
            throw new IncorrectOldPasswordException("Incorrect old password");
        }
    }

    @Override
    public void changeEmail(UserEmailChangeDto userEmailChangeDto) {
        User loggedUser = getLoggedUser();
        boolean emailTaken = userRepository.findByEmailAddress(userEmailChangeDto.email()).isPresent();
        if (emailTaken) {
            throw new EmailAddressTakenException("Email address is already taken.");
        } else {
            loggedUser.setEmailAddress(userEmailChangeDto.email());
            userRepository.save(loggedUser);
        }
    }

    @Override
    public void changeUsername(UsernameChangeDto usernameChangeDto) {
        User loggedUser = getLoggedUser();
        boolean nameTaken = userRepository.findByLogin(usernameChangeDto.name()).isPresent();
        if (nameTaken) {
            throw new UsernameTakenException("Username is already taken.");
        } else {
            loggedUser.setLogin(usernameChangeDto.name());
            userRepository.save(loggedUser);
        }
    }


    @Transactional
    @Override
    public void deleteAccount() {
        User loggedUser = getLoggedUser();
        userRepository.delete(loggedUser);
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
