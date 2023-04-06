package com.gymtracker.user;

import com.gymtracker.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    ResponseEntity<User> getLoggedUserInfo() {
        User loggedUser = userService.getLoggedUser();
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }
}
