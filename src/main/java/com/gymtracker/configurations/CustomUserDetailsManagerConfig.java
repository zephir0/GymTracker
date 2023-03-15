package com.gymtracker.configurations;

import com.gymtracker.user.UserLoginDto;
import com.gymtracker.user.UserRoles;
import com.gymtracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class CustomUserDetailsManagerConfig implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userService.findCredentialsByLogin(login)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login %s not found", login)));
    }

    private UserDetails createUserDetails(UserLoginDto userLoginDto) {
        return User.builder()
                .username(userLoginDto.login())
                .password(userLoginDto.password())
                .roles(UserRoles.ADMIN.name(), UserRoles.USER.name())
                .build();
    }

}
