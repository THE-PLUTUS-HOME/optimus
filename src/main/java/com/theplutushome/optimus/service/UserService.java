package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.UserAlreadyExistsException;
import com.theplutushome.optimus.advice.UserNotFoundException;
import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.dto.resetPassword.PasswordResetRequest;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.dto.login.LoginRequest;
import com.theplutushome.optimus.dto.login.LoginResponse;
import com.theplutushome.optimus.repository.UserRepository;
import com.theplutushome.optimus.util.BCryptUtil;
import com.theplutushome.optimus.util.Function;
import com.theplutushome.optimus.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User createUser(UserRequest userRequest) {
        if (userExists(userRequest.getUsername(), userRequest.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User user = new User(
                0,
                BCryptUtil.hashPassword(userRequest.getPassword()),
                userRequest.getEmail(),
                userRequest.getPhone(),
                userRequest.getUsername(),
                userRequest.getUserType(),
                UserAccountStatus.ACTIVE,
                userRequest.getSecretPhrase(),
                Function.generateReferralCode(),
                0.0

        );
        user.setDeleted(false);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setDeleted(true);
            user.get().setUserAccountStatus(UserAccountStatus.REVOKED);
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(UserNotFoundException::new);

        // Verify password
        if (!BCryptUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUsername());

        user.setLastLoggedIn(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(
                "success",
                "Login successful",
                user.getLastLoggedIn().toString(),
                token);
    }

    private boolean userExists(String username, String email) {
        return (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent());
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        User user = userRepository.findByUsername(passwordResetRequest.getUsername())
                .orElseThrow(UserNotFoundException::new);

        // Verify password
        if (!BCryptUtil.verifyPassword(passwordResetRequest.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password does not match");
        }

        user.setPassword(BCryptUtil.hashPassword(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
    }

}
