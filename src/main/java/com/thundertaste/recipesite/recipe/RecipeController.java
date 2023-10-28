package com.thundertaste.recipesite.recipe;


import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import com.thundertaste.recipesite.user.UserService;
import com.thundertaste.recipesite.user.UserTransferObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return "redirect:/recipes";
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

    // Search for recipes by name
    @GetMapping("/search")
    public String searchRecipes(@RequestParam("q") String keyword, Model model) {
        List<Recipe> recipes = recipeRepository.searchByName(keyword);
        model.addAttribute("recipes", recipes);
        model.addAttribute("searchedFor", keyword);
        return "recipes/searchResults";
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
                               @RequestParam("recipePhoto") MultipartFile file,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            // handle errors
        }

        // Getting the current logged-in username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        // Find user DTO based on the username
        UserTransferObject authorDto = userService.findUserByUsername(currentPrincipalName);

        // Check if author DTO was found
        if (authorDto == null) {
            // Handle the case when the user is not found
            return "redirect:/error"; // for example
        }

        // Now, you need to set the author of the recipe.
        // Assuming you have a method in your Recipe class to accept a UserTransferObject
        // Convert UserTransferObject to User entity
        User author = userService.convertToUserEntity(authorDto);
        recipe.setAuthor(author);
        // Continue as before...
        recipe.setDatePosted(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        // Handle file saving
        // ...

        // Save recipe, this should populate the recipe with an ID
        Recipe savedRecipe = recipeService.save(recipe);

        // Use RedirectAttributes to add flash attributes if needed
        // redirectAttributes.addFlashAttribute("message", "Recipe created successfully!");

        // Redirect to the recipe view page with the ID of the new recipe
        return "redirect:/recipe/" + savedRecipe.getId();
    }

}
