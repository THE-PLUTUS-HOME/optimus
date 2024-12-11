package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.dto.login.LoginRequest;
import com.theplutushome.optimus.dto.login.LoginResponse;
import com.theplutushome.optimus.dto.resetPassword.PasswordResetRequest;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/optimus/v1/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Sign Up", description = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                        {
                                            "id": 1,
                                            "name": "John Doe",
                                            "email": "john.doe@example.com"
                                        }
                                    """))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                        {
                                            "status": "404 NOT_FOUND",
                                            "message": "User with the given ID does not exist"
                                        }
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                        {
                                            "status": "500 INTERNAL_SERVER_ERROR",
                                            "message": "An unexpected error occurred"
                                        }
                                    """)))
    })
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
        userService.deleteUser(Integer.parseInt(id));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get User by ID", description = "Fetch a user based on their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest, @RequestHeader("Authorization") String authHeader) {
        userService.resetPassword(passwordResetRequest, authHeader);
        return ResponseEntity.ok().build();
    }
}


//}, @RequestHeader("Authorization") String authHeader
