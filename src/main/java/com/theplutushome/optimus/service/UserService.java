package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.UserAlreadyExistsException;
import com.theplutushome.optimus.advice.UserNotFoundException;
import com.theplutushome.optimus.dto.OtpRequestDto;
import com.theplutushome.optimus.entity.OTPRequest;
import com.theplutushome.optimus.dto.UserData;
import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.dto.login.LoginRequest;
import com.theplutushome.optimus.dto.login.LoginResponse;
import com.theplutushome.optimus.dto.resetPassword.PasswordResetRequest;
import com.theplutushome.optimus.entity.EntityModel;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.entity.enums.UserType;
import com.theplutushome.optimus.repository.OtpRepository;
import com.theplutushome.optimus.repository.UserRepository;
import com.theplutushome.optimus.util.BCryptUtil;
import com.theplutushome.optimus.util.Function;
import com.theplutushome.optimus.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;


    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, EmailService emailService, OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
    }

    @Transactional
    public void createUser(UserRequest userRequest) {
        // Check if the user already exists
        if (userExists(userRequest.getUsername(), userRequest.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        //Delete User if exits and inactive
        Optional<User> inActiveUser = userRepository.findByEmailAndUserAccountStatus(userRequest.getEmail(), UserAccountStatus.INACTIVE);
        inActiveUser.ifPresent(userRepository::delete);

        // Create the new user
        User user = new User(
                0,
                BCryptUtil.hashPassword(userRequest.getPassword()),
                userRequest.getEmail(),
                userRequest.getUsername(),
                UserType.USER,
                UserAccountStatus.INACTIVE,
                Function.generateReferralCode(),
                0.0 // Initial balance
        );
        user.setAccruedBalance(0.0);

        String otpCode = Function.randomOTPCode();
        OTPRequest otpRequest = new OTPRequest(user.getEmail(), otpCode);
        if(otpRepository.findByEmail(user.getEmail()).isPresent()){
            otpRepository.delete(otpRepository.findByEmail(user.getEmail()).get());
        }
        otpRepository.save(otpRequest);

        String emailContent = String.format("""
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border: 1px solid #dddddd;
                            border-radius: 8px;
                            overflow: hidden;
                        }
                        .header {
                            background-color: #007BFF;
                            color: #ffffff;
                            text-align: center;
                            padding: 20px;
                            font-size: 24px;
                        }
                        .content {
                            padding: 20px;
                            line-height: 1.6;
                            color: #333333;
                        }
                        .otp-code {
                            display: block;
                            margin: 20px auto;
                            text-align: center;
                            font-size: 32px;
                            font-weight: bold;
                            color: #007BFF;
                            background-color: #f9f9f9;
                            padding: 10px;
                            border: 1px dashed #007BFF;
                            width: fit-content;
                            border-radius: 8px;
                        }
                        .footer {
                            background-color: #f4f4f4;
                            text-align: center;
                            padding: 10px;
                            font-size: 12px;
                            color: #888888;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="header">
                            Welcome to The Plutus Home!
                        </div>
                        <div class="content">
                            <p>Hi %s,</p>
                            <p>We are excited to have you on board! To complete your registration, please use the OTP code below:</p>
                            <div class="otp-code">%s</div>
                            <p>If you did not request this, please ignore this email or contact our support team for assistance.</p>
                            <p>Thank you for choosing our service!</p>
                        </div>
                        <div class="footer">
                            &copy; 2024 The Plutus Home. All rights reserved.<br>
                            This is an automated email; please do not reply.
                        </div>
                    </div>
                </body>
                </html>
                """, user.getUsername(), otpCode);


        // Process referral logic
        String referralCode = userRequest.getReferralCode();
        if (referralCode != null && referralCodeValid(referralCode)) {
            User referralUser = userWithReferralCode(referralCode);
            if (referralUser != null) {
                referralUser.getReferredUsers().add(user);
                referralUser.setBalance(referralUser.getAccruedBalance() + 1.00); // Referral reward
                userRepository.save(referralUser); // Save changes to the referring user
            }
        }

        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "New Account Created", emailContent);
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
            throw new RuntimeException("No users found");
        }
        allUsers.removeIf(EntityModel::isDeleted);
        return allUsers;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByUsernameAndDeleted(loginRequest.getUsername(), false)
                .orElseGet(() -> userRepository.findByEmailAndDeleted(loginRequest.getUsername(), false)
                        .orElseThrow(UserNotFoundException::new));
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
                token,
                user.getEmail(),
                user.getUsername()
        );
    }

    private boolean userExists(String username, String email) {
        //There could be two instance of users because one is incative
        return (userRepository.findByEmailAndDeleted(email, false).isPresent()
                || userRepository.findByUsernameAndDeleted(username, false).isPresent());
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest, String authHeader) {
        jwtUtil.verifyToken(authHeader);

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

    public boolean referralCodeValid(String referralCode) {
        return userRepository.findByReferralCodeAndDeleted(referralCode, false).isPresent();
    }

    public User userWithReferralCode(String referralCode) {
        return userRepository.findByReferralCodeAndDeleted(referralCode, false).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserData getUserData(String username, String authHeader) {
        jwtUtil.verifyToken(authHeader);

        UserData userData = new UserData();
        User u = userRepository.findByUsernameAndDeleted(username, false).orElse(null);
        if (u != null) {
            userData.setUsername(u.getUsername());
            userData.setEmail(u.getEmail());
            userData.setReferralCode(u.getReferralCode());
            userData.setBalance(u.getBalance());
            userData.setAccruedBalance(u.getAccruedBalance());
            userData.setTotalReferrals(u.getReferredUsers().size());
        } else {
            throw new UserNotFoundException();
        }
        return userData;
    }

    public void redeemPoints(String username, String authHeader) {
        jwtUtil.verifyToken(authHeader);

        User user = userRepository.findByUsernameAndDeleted(username, false).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (user.getAccruedBalance() > 0) {
            var newBal = user.getBalance() + user.getAccruedBalance();
            user.setBalance(newBal);
            user.setAccruedBalance(0);
            userRepository.save(user);
        } else {
            throw new RuntimeException("No balance to redeem");
        }
    }

    @Transactional
    public void verifyAccountCreationOTP(OtpRequestDto otpRequest) {
        Optional<OTPRequest> otpInfo = otpRepository.findByEmail(otpRequest.email());
        if (otpInfo.isPresent()) {
            User user = userRepository.findByEmailAndDeleted(otpRequest.email(), false).orElse(null);
            if (user != null) {
                user.setUserAccountStatus(UserAccountStatus.ACTIVE);
                userRepository.save(user);

                otpInfo.get().setUsed(true);
                otpRepository.save(otpInfo.get());
            }
        } else {
            throw new RuntimeException("OTP Code Invalid");
        }

    }
}
