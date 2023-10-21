package com.thundertaste.recipesite.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create a new user
    public User createUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Ideally, hash the password before saving. You'd use a utility or a library like BCrypt here.
        user.setPasswordHash(/*hashing_function_here*/(user.getPasswordHash()));
        return userRepository.save(user);
    }

    // Fetch a user by ID
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    // Fetch all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user
    public User updateUser(User user) {
        if(!userRepository.existsById(user.getUserID())) {
            throw new RuntimeException("User not found!");
        }
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("User not found!");
        }
        userRepository.deleteById(id);
    }

    // Find by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
