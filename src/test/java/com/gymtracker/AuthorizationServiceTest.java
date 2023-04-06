package com.gymtracker;

import com.gymtracker.auth.AuthorizationService;
import com.gymtracker.auth.exception.UserAlreadyExistException;
import com.gymtracker.user.*;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserRegisterDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import com.gymtracker.user.mapper.UserRegisterMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRegisterMapper userRegisterMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void register_whenUserNotExists_shouldRegisterUser() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUser@gmail.com", "testUser", "testPassword", LocalDateTime.now(), UserRoles.USER);
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("testPassword");

        when(userRepository.existsByLogin(userRegisterDto.login())).thenReturn(false);
        when(userRegisterMapper.toEntity(userRegisterDto)).thenReturn(user);

        assertDoesNotThrow(() -> authorizationService.register(userRegisterDto));
        verify(userRepository).save(user);
    }

    @Test
    void login_whenValidCredentials_shouldAuthenticateUser() {
        UserLoginDto userLoginDto = new UserLoginDto("testUser", "testPassword");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password())))
                .thenReturn(authentication);

        assertDoesNotThrow(() -> authorizationService.login(userLoginDto));
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password()));
    }

    @Test
    void register_whenUserExists_shouldThrowUserAlreadyExistException() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("testUser@gmail.com", "testUser", "testPassword", LocalDateTime.now(), UserRoles.USER);

        when(userRepository.existsByLogin(userRegisterDto.login())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> authorizationService.register(userRegisterDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_whenInvalidCredentials_shouldThrowBadCredentialsException() {
        UserLoginDto userLoginDto = new UserLoginDto("testUser", "wrongPassword");

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password())))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authorizationService.login(userLoginDto));
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password()));
    }
}

