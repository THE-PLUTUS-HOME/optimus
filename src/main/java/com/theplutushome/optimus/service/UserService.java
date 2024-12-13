package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.UserAlreadyExistsException;
import com.theplutushome.optimus.advice.UserNotFoundException;
import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.dto.login.LoginRequest;
import com.theplutushome.optimus.dto.login.LoginResponse;
import com.theplutushome.optimus.dto.resetPassword.PasswordResetRequest;
import com.theplutushome.optimus.entity.EntityModel;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.exceptions.EmptyCollectionExceptiton;
import com.theplutushome.optimus.repository.UserRepository;
import com.theplutushome.optimus.util.BCryptUtil;
import com.theplutushome.optimus.util.Function;
import com.theplutushome.optimus.util.JwtUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createUser(UserRequest userRequest) {
        if (userExists(userRequest.getUsername(), userRequest.getEmail(), userRequest.getPhone())) {
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
        userRepository.save(user);
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
        var allUsers = userRepository.findAll();
        if (allUsers.isEmpty()) {
            throw new EmptyCollectionExceptiton();
        }
        allUsers.removeIf(EntityModel::isDeleted);
        return allUsers;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndDeleted(loginRequest.getUsername(), false)
                .orElseThrow(UserNotFoundException::new);

        // Verify password
        if (!BCryptUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername());

        user.setLastLoggedIn(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(
                "success",
                "Login successful",
                user.getLastLoggedIn().toString(),
                token);
    }

    private boolean userExists(String username, String email, String phone) {
        return (userRepository.findByEmailAndDeleted(email, false).isPresent()
                || userRepository.findByUsernameAndDeleted(username, false).isPresent()
                || userRepository.findByEmailAndDeleted(email, false).isPresent());
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest, String authHeader) {
        if (authHeader.isBlank() || authHeader.isEmpty()) {
            throw new JwtException("Missing authentication header");
        }

        try {
            if (jwtUtil.isTokenExpired(authHeader)) {
                Map<String, Object> headerValues = new HashMap<>();
                headerValues.put("alg", "HS256"); // Algorithm
                headerValues.put("typ", "JWT");  // Token type

                Header header = new DefaultHeader(headerValues);
                Claims claims = jwtUtil.extractClaim(authHeader); // Ensure this method properly extracts claims
                throw new ExpiredJwtException(header, claims, "Token has expired");
            }
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT structure: " + ex.getMessage());
        } catch (SignatureException ex) {
            throw new JwtException("Invalid JWT signature: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Invalid or empty JWT: " + ex.getMessage());
        }

        User user = userRepository.findByUsernameAndDeleted(passwordResetRequest.getUsername(), false)
                .orElseThrow(UserNotFoundException::new);
        System.out.println("trying to reset password");
        // Verify password
        if (!BCryptUtil.verifyPassword(passwordResetRequest.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(BCryptUtil.hashPassword(passwordResetRequest.getNewPassword()));
        userRepository.save(user);
    }

}
