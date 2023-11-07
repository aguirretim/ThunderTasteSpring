package com.thundertaste.recipesite.recipe;


import com.thundertaste.recipesite.core.ImageService;
import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import com.thundertaste.recipesite.user.UserService;
import com.thundertaste.recipesite.user.UserTransferObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Controller
//@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService; // Add this line

    @Autowired
    private ImageService imageService;

    // List all recipes
    @GetMapping("/recipes")
    public String listAllRecipes(Model model) {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        model.addAttribute("recipes", recipes);
        return "recipes/list";
    }


    // Add a new recipe
    @GetMapping("/add")
    public String showAddRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "recipes/add";
    }

    @PostMapping("/add")
    public String addRecipe(@ModelAttribute Recipe recipe) {
        recipeRepository.save(recipe);
        return "redirect:/index";
    }

    // Update an existing recipe
    @GetMapping("/edit/{id}")
    public String showEditRecipeForm(@PathVariable Long id, Model model) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);
        if (recipeOpt.isPresent()) {
            model.addAttribute("recipe", recipeOpt.get());
            return "recipes/edit";
        } else {
            return "error/recipeNotFound";
        }
    }

    @PostMapping("/edit/{id}")
    public String editRecipe(@PathVariable Long id, @ModelAttribute Recipe recipe) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.save(recipe);
            return "redirect:/recipes/" + id;
        } else {
            return "error/recipeNotFound";
        }
    }

    // Delete a recipe
    @GetMapping("/delete/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return "redirect:/recipes";
        } else {
            return "error/recipeNotFound";
        }
    }
    @GetMapping("recipe/{id}")
    public String getRecipe(Model model, @PathVariable Long id) {
        Optional<Recipe> recipeOptional = recipeService.findById(id);

        if (!recipeOptional.isPresent()) {
            // Handle the case where the recipe is not found
            // For example, redirect to a "not found" page or return an error message
            return "recipeNotFound";
        }

        model.addAttribute("recipe", recipeOptional.get());
        return "recipe-view"; // name of your HTML file
    }


    @GetMapping("/submit-recipe")
    public String displaySubmitRecipes(Model model) {
        Recipe recipe = new Recipe();  // Recipe should have a title field
        model.addAttribute("recipe", recipe);

        return "submit-recipe";
    }


    @PostMapping("/submit-recipe")
    public String submitRecipe(@ModelAttribute("recipe") Recipe recipe,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication) {
        try {
            String currentPrincipalName = authentication.getName();
            UserTransferObject authorDto = userService.findUserByUsername(currentPrincipalName);

            if (authorDto == null) {
                return "redirect:/error";
            }

            // Convert UserTransferObject to User entity
            User author = userService.convertToUserEntity(authorDto);
            recipe.setAuthor(author);
            recipe.setDatePosted(new Date());

            // Check if the image file isn't empty and save it using ImageService
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = imageService.saveImage(imageFile); // The method to save the image and return the path
                recipe.setImage(imagePath); // Set the image path of the recipe
            }

            // Save the recipe and get the saved entity back to capture the generated ID
            Recipe savedRecipe = recipeService.save(recipe);

            redirectAttributes.addFlashAttribute("message", "Recipe submitted successfully!");
            return "redirect:/recipe/" + savedRecipe.getId();

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting recipe. Please try again.");
            return "submit-recipe";
        }
    }


    @GetMapping("/images/recipePhotos/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path uploadDirectory = Paths.get("src/main/resources/static/images/recipePhotos"); // Make sure this path is correct.
        System.out.println("Absolute path: " + uploadDirectory.toAbsolutePath().toString());
        System.out.println("Requested filename: " + filename);
        Path file = uploadDirectory.resolve(filename);
        if (!Files.exists(file)) {
            throw new RuntimeException("File not found: " + file);
        }
        if (!Files.isReadable(file)) {
            throw new RuntimeException("File not readable: " + file);
        }

        System.out.println("Current directory: " + Paths.get("").toAbsolutePath().toString());

        URI uri = file.toUri();
        System.out.println("File URI: " + uri);


        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            // Set the correct content type and inline content disposition
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }




    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Recipe> recipes;
        if (query != null && !query.isEmpty()) {
            recipes = recipeService.searchRecipes(query);
        } else {
            // Handle the case when query is not provided
            // Example: Return all recipes or a default set
            recipes = recipeService.getAllRecipes(); // Ensure you have a method to handle this
        }
        model.addAttribute("recipes", recipes);
        return "recipe-search"; // Name of the Thymeleaf template
    }

    private void saveFile(MultipartFile file) throws IOException {
        String uploadDir = "/path/to/uploads";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + file.getOriginalFilename(), ioe);
        }
    }


    @GetMapping("/my-recipes")
    public String myRecipes(Model model, Principal principal) {
        // Fetch the User DTO using the username
        UserTransferObject userDto = userService.findUserByUsername(principal.getName());
        if (userDto == null) {
            // Handle the case where the user DTO is not found
            // Redirect to login
            return "redirect:/login";
        }

        // Ensure your DTO has a method to get the userID
        Long userId = userDto.getUserID(); // replace getUserID with your actual method to get ID from DTO

        // Fetch the recipes using the userID
        List<Recipe> personalRecipes = recipeService.findRecipesByUserId(userId);
        model.addAttribute("recipes", personalRecipes);
        return "my-recipes"; // Name of your Thymeleaf template
    }

}
