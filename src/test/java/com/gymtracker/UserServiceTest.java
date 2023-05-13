package com.gymtracker;

import com.gymtracker.user.entity.User;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.UserRepository;
import com.gymtracker.user.UserService;
import com.gymtracker.user.exception.UserNotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    private User user;
    private UserLoginDto userLoginDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setLogin("testUser");
        user.setPassword("testUser");
        userLoginDto = new UserLoginDto("testUser", "testUser");
    }

    @Test
    void findCredentialsByLogin_whenUserExists_returnsUserLoginDto() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));

        Optional<UserLoginDto> result = userService.getCredentialsByLogin("testUser");

        assertEquals(userLoginDto, result.get());
    }

    @Test
    void findCredentialsByLogin_whenUserDoesNotExist_returnsEmptyOptional() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        Optional<UserLoginDto> result = userService.getCredentialsByLogin("nonExistentUser");

        assertEquals(Optional.empty(), result);
    }

    @Test
    @WithMockUser
    void getLoggedUser_whenUserIsAuthenticated_returnsUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = mock(Authentication.class);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");
        securityContext.setAuthentication(authentication);


        User result = userService.getLoggedUser();

        assertEquals(user, result);
    }


    @Test
    @WithMockUser
    void getLoggedUser_whenUserIsNotAuthenticated_throwsUserNotLoggedInException() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = mock(Authentication.class);
        securityContext.setAuthentication(authentication);

        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(UserNotLoggedInException.class, () -> userService.getLoggedUser());
    }


}
