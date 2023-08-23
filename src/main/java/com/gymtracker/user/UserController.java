package com.gymtracker.user;

import com.gymtracker.user.dto.UserResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping("/logged-user")
    @ApiOperation("Get details of the logged-in user")
    ResponseEntity<UserResponseDto> getLoggedUserInfo() {
        UserResponseDto loggedUserInfo = userService.getDetailsOfLoggedUser();
        return new ResponseEntity<>(loggedUserInfo, HttpStatus.OK);
    }
}
