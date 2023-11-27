package com.gymtracker.auth;

import com.gymtracker.auth.exception.UserAlreadyExistException;
import com.gymtracker.auth.token.JwtTokenProvider;
import com.gymtracker.auth.token.JwtTokenStore;
import com.gymtracker.user.UserRepository;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserRegisterDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.mapper.UserRegisterMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRegisterMapper userRegisterMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private JwtTokenStore jwtTokenStore;

    @InjectMocks
    private AuthorizationService authorizationService;

    private UserRegisterDto userRegisterDto;
    private UserLoginDto userLoginDto;

    @Before
    public void setUp() {
        userRegisterDto = new UserRegisterDto("testLogin", "testEmail@example.com", "testPassword");
        userLoginDto = new UserLoginDto("testLogin", "testPassword");
    }

    @Test
    public void testRegister_Success() {
        // Arrange
        when(userRepository.existsByLoginOrEmailAddress(userRegisterDto.login(), userRegisterDto.emailAddress())).thenReturn(false);
        when(userRegisterMapper.toEntity(userRegisterDto)).thenReturn(new User());

        // Act
        authorizationService.register(userRegisterDto);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = UserAlreadyExistException.class)
    public void testRegister_UserAlreadyExists() {
        // Arrange
        when(userRepository.existsByLoginOrEmailAddress(userRegisterDto.login(), userRegisterDto.emailAddress())).thenReturn(true);

        // Act
        authorizationService.register(userRegisterDto);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password()))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("someJwtToken");

        // Act
        String token = authorizationService.login(userLoginDto);

        // Assert
        assertEquals("someJwtToken", token);
    }
}
