package com.gymtracker.auth;

import com.gymtracker.auth.exception.UserAlreadyExistException;
import com.gymtracker.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
@Validated
public class AuthorizationService {
    private final AuthenticationManager authenticationManager;
    private final UserRegisterMapper userRegisterMapper;
    private final UserRepository userRepository;

    public void register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByLogin(userRegisterDto.login())) {
            throw new UserAlreadyExistException(String.format("User with login : '%s' already exist in database", userRegisterDto.login()));
        }
        User user = userRegisterMapper.toEntity(userRegisterDto);
        userRepository.save(user);
    }

    public void login(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    public void logout(HttpServletRequest httpRequest) {
//        SecurityContextHolder.getContext().setAuthentication(null);
//        httpRequest.getSession().invalidate();
//    }


}
