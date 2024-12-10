package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.UserAlreadyExistsException;
import com.theplutushome.optimus.advice.UserNotFoundException;
import com.theplutushome.optimus.dto.UserRequest;
import com.theplutushome.optimus.entity.User;
import com.theplutushome.optimus.entity.enums.UserAccountStatus;
import com.theplutushome.optimus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequest userRequest) {
        if (userExists(userRequest.getUsername(), userRequest.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User user = new User(
                0,
                userRequest.getName(),
                userRequest.getPassword(),
                userRequest.getEmail(),
                userRequest.getUsername(),
                userRequest.getUserType(),
                UserAccountStatus.ACTIVE,
                userRequest.getSecretPhrase()
        );
        user.setCreatedAt(LocalDateTime.now());
        user.setDeleted(false);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setDeleted(true);
            user.get().setUserAccountStatus(UserAccountStatus.REVOKED);
            user.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException();
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    private boolean userExists(String username, String email) {
        return (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent());
    }

}
