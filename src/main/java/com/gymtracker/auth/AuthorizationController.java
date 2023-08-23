package com.gymtracker.auth;

import com.gymtracker.response.SuccessResponse;
import com.gymtracker.user.dto.UserLoginDto;
import com.gymtracker.user.dto.UserRegisterDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Api(tags = "Authorization API")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @ApiOperation("Login to the application")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated UserLoginDto userLoginDto) {
        return authorizationService.login(userLoginDto);
    }

    @ApiOperation("Register a new user")
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@RequestBody @Validated UserRegisterDto userRegisterDto) {
        authorizationService.register(userRegisterDto);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.CREATED, "User registered successfully.", LocalDateTime.now());
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }
}
