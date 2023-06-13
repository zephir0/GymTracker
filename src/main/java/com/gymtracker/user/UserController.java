package com.gymtracker.user;

import com.gymtracker.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/logged-user")
    ResponseEntity<UserResponseDto> getLoggedUserInfo() {
        UserResponseDto loggedUserInfo = userService.getDetailsOfLoggedUser();
        return new ResponseEntity<>(loggedUserInfo, HttpStatus.OK);
    }
}
