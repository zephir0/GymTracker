package com.gymtracker.user;

import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.entity.UserRoles;
import com.gymtracker.user.exception.UserNotLoggedInException;
import com.gymtracker.user.mapper.UserMapper;
import com.gymtracker.user.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private final String username = "user";
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper);

        user = new User();
        user.setLogin(username);
        user.setPassword("test");

        userResponseDto = new UserResponseDto("@mail", "login", UserRoles.USER);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetCredentialsByLogin_UserNotFound() {
        when(userRepository.findByLogin(username)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), userService.getCredentialsByLogin(username));
    }

    @Test
    public void testGetCredentialsByLogin_Success() {
        UserLoginDto userLoginDto = new UserLoginDto("user", "test");

        assertEquals(Optional.of(userLoginDto), userService.getCredentialsByLogin(username));
    }

    @Test
    public void testGetLoggedUser_UserNotLoggedIn() {
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(UserNotLoggedInException.class, userService::getLoggedUser);
    }

    @Test
    public void testGetLoggedUser_Success() {
        assertEquals(user, userService.getLoggedUser());
    }

    @Test
    public void testGetDetailsOfLoggedUser_Success() {
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        assertEquals(userResponseDto, userService.getDetailsOfLoggedUser());
    }

    @Test
    public void testGetReference_Success() {
        Long userId = 1L;
        when(userRepository.getReferenceById(userId)).thenReturn(user);

        assertEquals(user, userService.getReference(userId));
    }
}
