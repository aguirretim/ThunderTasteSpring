package com.thundertaste.recipesite.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isPresent()) {
                UserTransferObject userDto = UserTransferObject.fromUser(userOptional.get());
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

        // Add the profile image path to the model (adjust the getProfileImage() method if necessary)
        // This assumes that your User object has a 'getProfileImage' method that returns the path of the image
        String profileImagePath = user.getProfileImage(); // or user.getProfileImagePath();
        model.addAttribute("profileImagePath", profileImagePath);

        if (user.getBio() == null) {
            user.setBio("");
        }
        return "my-account";
    }

    @GetMapping("/images/profileImages/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path uploadDirectory = Paths.get("/images/profileImages/");
        Path file = uploadDirectory.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }




    @PostMapping("/update-profile")
    public String updateUserProfile(
            @RequestParam("bio") String bio,
            @RequestParam("profileImage") MultipartFile profileImage,
            Principal principal,
            RedirectAttributes attributes) throws IOException {

        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update bio
            user.setBio(bio);

            // Handle profile image upload
            if (!profileImage.isEmpty()) {
                String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
                String fileExtension = StringUtils.getFilenameExtension(fileName);
                String generatedFilename = UUID.randomUUID().toString();
                fileName = generatedFilename + (fileExtension != null ? "." + fileExtension : "");
                // Define the local path where the image should be stored
                Path path = Paths.get("src/main/resources/static/images/profileImages/" + fileName);
                // The directory should already exist, you can also make the application to create it on startup
                Path uploadDirectory = Paths.get("/images/profileImages/");
                if (!Files.exists(uploadDirectory)) {
                    Files.createDirectories(uploadDirectory);
                }

                Path filePath = uploadDirectory.resolve(fileName);
                Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setProfileImage(filePath.toString());
                try {
                    // Save the file locally in the resources/static/images/profiles directory
                    Files.copy(profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    // Now, you need to set the file path to the user object
                    //  `setProfileImagePath` is a method in your User class+ "?t=" + System.currentTimeMillis()
                    user.setProfileImage("/images/profileImages/" + fileName);
                    user.setProfileImage(filePath.toString());

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

