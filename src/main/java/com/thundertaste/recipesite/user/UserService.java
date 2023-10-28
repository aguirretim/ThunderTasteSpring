package com.thundertaste.recipesite.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
    // Fetch a user by ID and convert to DTO
    public Optional<UserTransferObject> getUserAsDTO(Long id) {
        return userRepository.findById(id).map(UserTransferObject::fromUser);
    }
    // Fetch all users
    public List<UserTransferObject> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserTransferObject::new)
                .collect(Collectors.toList());
    }

    // Fetch all users as DTOs
    public List<UserTransferObject> getAllUsersAsDTOs() {
        return userRepository.findAll().stream()
                .map(UserTransferObject::fromUser)
                .collect(Collectors.toList());
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

    // Find by username and convert to DTO
    public Optional<UserTransferObject> findUserTransferObjectByUsername(String username) {
        return userRepository.findByUsername(username).map(UserTransferObject::fromUser);
    }
    // Find by email
    // Find by email and convert to DTO
    public Optional<UserTransferObject> findUserTransferObjectByEmail(String email) {
        return userRepository.findByEmail(email).map(UserTransferObject::fromUser);
    }

    // Find by username and return DTO
    public Optional<UserTransferObject> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserTransferObject::new);
    }

    // Find by email
    public Optional<UserTransferObject> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserTransferObject::new); // Convert User to UserTransferObject
    }

    // Find by username and return DTO
    public UserTransferObject findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserTransferObject::new)
                .orElse(null);
    }

    public User convertToUserEntity(UserTransferObject userDto) {
        // Assuming userDto has a method getUsername() to get the username
        // And your UserRepository has a method findByUsername to fetch the User entity
        return userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
