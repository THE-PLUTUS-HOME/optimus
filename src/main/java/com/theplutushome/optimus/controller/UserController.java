package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.dto.login.LoginRequest;
import com.theplutushome.optimus.dto.login.LoginResponse;
import com.theplutushome.optimus.dto.resetPassword.PasswordResetRequest;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/optimus/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/list")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(Integer.valueOf(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest, @RequestHeader("Authorization") String authHeader) {
        userService.resetPassword(passwordResetRequest);
        return ResponseEntity.ok().build();
    }
}


//}, @RequestHeader("Authorization") String authHeader
