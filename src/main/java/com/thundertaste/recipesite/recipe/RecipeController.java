package com.thundertaste.recipesite.recipe;


import com.thundertaste.recipesite.core.ImageService;
import com.thundertaste.recipesite.rating.Rating;
import com.thundertaste.recipesite.rating.RatingService;
import com.thundertaste.recipesite.review.Review;
import com.thundertaste.recipesite.review.ReviewService;
import com.thundertaste.recipesite.review.ReviewTransferObject;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.*;


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

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RatingService ratingService;


    @GetMapping({"/", "/home", "/index"})
    public String displayHome(Model model) {
        List<Recipe> recipes = recipeService.getNewestRecipes();
        Map<Long, Double> averageRatings = new HashMap<>();

        for (Recipe recipe : recipes) {
            List<Review> reviews = reviewService.findReviewsByRecipeId(recipe.getId());
            double averageRating = calculateAverageRating(reviews);
            averageRatings.put(recipe.getId(), averageRating);
        }

        Map<Long, List<Boolean>> starData = prepareStarData(averageRatings);

        model.addAttribute("recipes", recipes);
        model.addAttribute("averageRatings", averageRatings);
        model.addAttribute("starData", starData);
        return "index";
    }


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
            model.addAttribute("notFound", true);
            model.addAttribute("message", "The requested recipe does not exist.");
            return "recipeNotFound";
        }
        Recipe recipe = recipeOptional.get();

        // Fetch the reviews and their associated authors
        List<ReviewTransferObject> reviewDTOs = reviewService.getReviewsWithAuthors(id);
        Map<Long, Double> averageRatings = new HashMap<>();

        model.addAttribute("recipe", recipe);
        // Fetch the reviews and ratings for this recipe
        List<Review> reviews = reviewService.findReviewsByRecipeId(id);
        List<Rating> ratings = ratingService.findRatingsByRecipeId(id); // If ratings are separate
        double averageRating = calculateAverageRating(reviews); // Method to calculate average rating from the list of reviews
        averageRatings.put(recipe.getId(), averageRating);
        Map<Long, List<Boolean>> starData = prepareStarData(averageRatings);

        model.addAttribute("reviewDTOs", reviewDTOs);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("starData", starData);
        model.addAttribute("notFound", false);

        return "recipe-view";
    }


    @GetMapping("/submit-recipe")
    public String displaySubmitRecipes(Model model) {
        Recipe recipe = new Recipe();
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
            }else {
                // Set default image path if no image is uploaded
                recipe.setImage("images/recipePhotos/noRecipeImage.png");
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
            recipes = recipeService.getAllRecipes();
        }

        Map<Long, Double> averageRatings = new HashMap<>();

        for (Recipe recipe : recipes) {
            List<Review> reviews = reviewService.findReviewsByRecipeId(recipe.getId());
            // Filter out reviews with null ratings
            reviews.removeIf(review -> review.getRating() == null);
            double averageRating = calculateAverageRating(reviews);
            averageRatings.put(recipe.getId(), averageRating);
            // Log each recipe's average rating
            System.out.println("Recipe ID: " + recipe.getId() + ", Average Rating: " + averageRating);
        }
        Map<Long, List<Boolean>> starData = prepareStarData(averageRatings);

        // Log the star data
        System.out.println("Star Data: " + starData);

        model.addAttribute("recipes", recipes);
        model.addAttribute("averageRatings", averageRatings);
        model.addAttribute("starData", starData);

        System.out.println("Average Ratings Map: " + averageRatings);
        System.out.println("Star Data Map: " + starData);

        return "recipe-search"; // Replace with your actual view name
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
        Map<Long, Double> averageRatings = new HashMap<>();
        for (Recipe recipe : personalRecipes) {
            List<Review> reviews = reviewService.findReviewsByRecipeId(recipe.getId());
            double averageRating = calculateAverageRating(reviews);
            averageRatings.put(recipe.getId(), averageRating);
        }

        Map<Long, List<Boolean>> starData = prepareStarData(averageRatings);


        model.addAttribute("recipes", personalRecipes);
        model.addAttribute("starData", starData);
        return "my-recipes"; // Name of your Thymeleaf template
    }



    @GetMapping("/recipe/{recipeId}/create-a-review")
    public String reviewPage(@PathVariable Long recipeId, Model model) {
        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);

        if (optionalRecipe.isEmpty()) {
            model.addAttribute("notFound", true);
            model.addAttribute("message", "The requested recipe does not exist.");
            // You can still return the same view but with a message that recipe is not found
            return "create-a-review";
        }

        Recipe recipe = optionalRecipe.get();
        Rating rating = Rating.createRating();
        Review review = Review.createReview();

        model.addAttribute("recipe", recipe);
        model.addAttribute("review", review);
        model.addAttribute("rating", rating);
        model.addAttribute("notFound", false);

        return "create-a-review";
    }


    @PostMapping("/recipe/{recipeId}/submit-review")
    public String submitReview(@PathVariable Long recipeId,
                               @ModelAttribute("review") Review review,
                               @ModelAttribute("rating") Rating rating,
                               Model model,
                               Principal principal) {
        // logging to check the value of 'star'
        System.out.println("Star rating received: " + rating.getScore());

        model.addAttribute("review", Review.createReview());
        model.addAttribute("rating", Rating.createRating());

        Optional<Recipe> optionalRecipe = recipeService.findById(recipeId);
        if (optionalRecipe.isEmpty()) {
            return "redirect:/";
        }

        // Fetch the UserTransferObject or User using the principal.getName()
        UserTransferObject user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Assuming UserTransferObject has a getId() method to fetch the User's ID
        Long userId = user.getUserID();

        review.setUserID(userId); // Set the user ID for the review
        review.setRecipeID(recipeId); // Set the recipe ID for the review
        review.setDatePosted(new Date()); // Set the current date for the review
        review.setRating(rating);


        rating.setUserID(userId); // Set the user ID for the rating
        rating.setRecipeID(recipeId); // The score should already be set in the 'rating' object if your form is set up correctly
        rating.setDateGiven(new Date());
        ratingService.save(rating); // Save the rating

        reviewService.save(review); // Save the review


        return "redirect:/recipe/" + recipeId;
    }


    // Utility method to calculate average rating, assuming Review has a method getRating() which returns Rating object
    private double calculateAverageRating(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 0; // No reviews
        }
        double sum = 0.0;
        int count = 0;
        for (Review review : reviews) {
            if (review.getRating() != null) {
                sum += review.getRating().getScore();
                count++;
            }
        }
        return count == 0 ? -1 : sum / count; // -1 if reviews exist but no ratings

    }

    private Map<Long, List<Boolean>> prepareStarData(Map<Long, Double> averageRatings) {
        Map<Long, List<Boolean>> starData = new HashMap<>();
        for (Map.Entry<Long, Double> entry : averageRatings.entrySet()) {
            Long recipeId = entry.getKey();
            Double rating = entry.getValue();
            List<Boolean> stars = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                stars.add(i <= rating);
            }
            starData.put(recipeId, stars);

            // Log each recipe's star data
            System.out.println("Recipe ID: " + recipeId + ", Stars: " + stars);
        }
        return starData;
    }


}
