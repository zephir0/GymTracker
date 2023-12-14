package com.gymtracker.user;

import com.gymtracker.response_model.SuccessResponse;
import com.gymtracker.user.dto.UserEmailChangeDto;
import com.gymtracker.user.dto.UserPasswordChangeDto;
import com.gymtracker.user.dto.UserResponseDto;
import com.gymtracker.user.dto.UsernameChangeDto;
import com.gymtracker.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Api(tags = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping("/logged-user")
    @ApiOperation("Get details of the logged-in user")
    @ResponseStatus(HttpStatus.OK)
    UserResponseDto getLoggedUserInfo() {
        return userService.getDetailsOfLoggedUser();
    }

    @DeleteMapping("/delete-account")
    @ApiOperation("Delete an account")
    @ResponseStatus(HttpStatus.OK)
    SuccessResponse deleteAccount() {
        userService.deleteAccount();
        return new SuccessResponse(HttpStatus.OK, "User account has been deleted.", LocalDateTime.now());
    }

    @PutMapping("/change-password")
    @ApiOperation("Change password")
    @ResponseStatus(HttpStatus.OK)
    SuccessResponse changePassword(@RequestBody @Validated UserPasswordChangeDto userPasswordChangeDto) {
        userService.changePassword(userPasswordChangeDto);
        return new SuccessResponse(HttpStatus.OK, "User password has been changed.", LocalDateTime.now());
    }

    @PutMapping("/change-email")
    @ApiOperation("Change an email address")
    @ResponseStatus(HttpStatus.OK)
    SuccessResponse changeEmail(@RequestBody @Validated UserEmailChangeDto userEmailChangeDto) {
        userService.changeEmail(userEmailChangeDto);
        return new SuccessResponse(HttpStatus.OK, "User email address has been changed.", LocalDateTime.now());
    }

    @PutMapping("/change-username")
    @ApiOperation("Change username")
    @ResponseStatus(HttpStatus.OK)
    SuccessResponse changeUsername(@RequestBody @Validated UsernameChangeDto newUsername) {
        userService.changeUsername(newUsername);
        return new SuccessResponse(HttpStatus.OK, "User email address has been changed.", LocalDateTime.now());
    }
}
