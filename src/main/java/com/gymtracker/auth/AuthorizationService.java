package com.gymtracker.auth;

import com.gymtracker.auth.exception.UserAlreadyExistException;
import com.gymtracker.auth.token.JwtTokenProvider;
import com.gymtracker.user.UserRepository;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserRegisterDto;
import com.gymtracker.user.entity.User;
import com.gymtracker.user.mapper.UserRegisterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService {
    private final AuthenticationManager authenticationManager;
    private final UserRegisterMapper userRegisterMapper;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void register(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByLoginOrEmailAddress(userRegisterDto.login(), userRegisterDto.emailAddress())) {
            throw new UserAlreadyExistException(String.format("User with login : '%s' or email : '%s' already exists in database", userRegisterDto.login(), userRegisterDto.emailAddress()));
        }
        User user = userRegisterMapper.toEntity(userRegisterDto);
        userRepository.save(user);
    }

    public String login(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.login(), userLoginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

}
