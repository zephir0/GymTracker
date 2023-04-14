package com.gymtracker.user;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/logged-user")
    ResponseEntity<SuccessResponse> getLoggedUserInfo() {
        UserResponseDto loggedUserInfo = userService.getLoggedUserInfo();
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, loggedUserInfo, LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
