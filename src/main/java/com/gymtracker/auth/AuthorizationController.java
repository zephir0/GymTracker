package com.gymtracker.auth;

import com.gymtracker.response_model.SuccessResponse;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserRegisterDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(tags = "Authorization API")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @ApiOperation("Login to the application")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody @Validated UserLoginDto userLoginDto) {
        return authorizationService.login(userLoginDto);
    }

    @ApiOperation("Register a new user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse register(@RequestBody @Validated UserRegisterDto userRegisterDto) {
        authorizationService.register(userRegisterDto);
        return new SuccessResponse(HttpStatus.CREATED, "User registered successfully.", LocalDateTime.now());
    }
}
