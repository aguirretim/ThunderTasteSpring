package com.thundertaste.recipesite.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        if (user.getBio() == null) {
            user.setBio("");
        }
        return "my-account";

    }

    @PostMapping("/update-profile")
    public String updateUserProfile(
            @RequestParam("bio") String bio,
            @RequestParam("profileImage") MultipartFile profileImage,
            Principal principal,
            RedirectAttributes attributes) {

        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update bio
            user.setBio(bio);

            // Handle profile image upload
            if (!profileImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
                // Define the local path where the image should be stored
                Path path = Paths.get("src/main/resources/static/images/profileImages/" + fileName);
                try {
                    // Save the file locally in the resources/static/images/profiles directory
                    Files.copy(profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    // Now, you need to set the file path to the user object
                    // Assume `setProfileImagePath` is a method in your User class
                    user.setProfileImage("/images/profileImages/" + fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                    attributes.addFlashAttribute("errorMessage", "Could not save profile image");
                    return "redirect:/my-account";
                }
            }

            // Save the user information along with the bio and possibly the new image path
            userRepository.save(user);

            // After saving the image and bio, add attributes to show a success message
            attributes.addFlashAttribute("user", user);
            attributes.addFlashAttribute("profileUpdated", true);
            attributes.addFlashAttribute("message", "Profile updated successfully!");

        } else {
            attributes.addFlashAttribute("errorMessage", "User not found");
        }

        return "redirect:/my-account";
    }



    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processRegistration(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }
        // Save user or handle signup logic

        try {
            userService.createUser(user);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }

        // Redirect to a confirmation page, or perhaps the login page
        return "redirect:/login";
    }



}

