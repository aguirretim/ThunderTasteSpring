package com.thundertaste.recipesite.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<UserTransferObject> userDto  = userService.getUserAsDTO(id) ;
        if (userDto.isPresent()) {
            return new ResponseEntity<>(userDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<UserTransferObject>> getAllUsers() {
        List<UserTransferObject> userDtos = userService.getAllUsers();
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        if (!id.equals(updatedUser.getUserID())) {
            return new ResponseEntity<>("User ID mismatch", HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.updateUser(updatedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        Optional<UserTransferObject> userDto = userService.findByUsername(username);
        if (userDto.isPresent()) {
            return new ResponseEntity<>(userDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Optional<UserTransferObject> userDto = userService.findByEmail(email);
        if (userDto.isPresent()) {
            return new ResponseEntity<>(userDto.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/my-account")
    public String viewProfile(Model model) {
        // Get logged-in username
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch user details from the database
        User user = userRepository.findByUsername(loggedInUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Add user details to the model
        model.addAttribute("user", user);

        return "my-account";

    }

    @PostMapping("/update-profile")
    public String updateUserProfile(@RequestParam("bio") String bio, Principal principal, Model model) {
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setBio(bio);
            userRepository.save(user);
            model.addAttribute("user", user);
            model.addAttribute("profileUpdated", true);
        } else {
            model.addAttribute("errorMessage", "User not found");
        }
        return "my-account";
    }

}

