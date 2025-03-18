package com.money.api.controller;

import com.money.api.entity.User;
import com.money.api.entity.dto.UserResponse;
import com.money.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserInfo(){
        User user = userService.getCurrentUser();
        UserResponse userResponse = new UserResponse(user);

        return ResponseEntity.ok(userResponse);
    }
}
